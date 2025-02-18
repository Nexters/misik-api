package me.misik.api.app;

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import me.misik.api.api.response.ParsedOcrResponse
import me.misik.api.core.Chatbot
import me.misik.api.core.GracefulShutdownDispatcher
import me.misik.api.core.OpenAIOcrParser
import me.misik.api.domain.CreateReviewCache
import me.misik.api.domain.Review
import me.misik.api.domain.ReviewService
import me.misik.api.domain.Style
import me.misik.api.domain.prompt.PromptService
import me.misik.api.domain.request.CreateReviewRequest
import me.misik.api.domain.request.OcrTextRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.milliseconds

@Service
class CreateReviewFacade(
    private val chatbot: Chatbot,
    private val openAiOcrParser: OpenAIOcrParser,
    private val reviewService: ReviewService,
    private val promptService: PromptService,
    private val createReviewCache: CreateReviewCache,
) {

    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    fun createReviewInBackground(deviceId: String, createReviewRequest: CreateReviewRequest): Long {
        val prompt = promptService.getByStyle(createReviewRequest.reviewStyle)
        val review = reviewService.createReview(deviceId, prompt.command, createReviewRequest)

        createReviewCache.put(review.id, review)

        createReviewWithRetry(review, retryCount = 0)

        return review.id
    }

    private fun createReviewWithRetry(review: Review, retryCount: Int) {
        CoroutineScope(GracefulShutdownDispatcher.dispatcher).launch {
            chatbot.createReviewWithModelName(Chatbot.Request.from(review))
                .filterNot { it.stopReason == ALREADY_COMPLETED }
                .collect {
                    val newText = it.message?.content ?: ""
                    review.addText(newText)

                    val updatedReview = review.copy()
                    createReviewCache.put(review.id, updatedReview)
                }
        }.invokeOnCompletion {
            if (it == null) {
                logger.info("Review created successfully. ${review.text}")
                createReviewCache.get(review.id).let {
                    reviewService.updateAndCompleteReview(it.id, it.text)
                }
                createReviewCache.remove(review.id)
                return@invokeOnCompletion
            }
            if (retryCount == MAX_RETRY_COUNT) {
                logger.error("Failed to create review.", it)
                createReviewCache.remove(review.id)
                throw it
            }
            logger.warn(
                "Failed to create review. retrying... retryCount: \"${retryCount + 1}\"",
                it
            )
            createReviewWithRetry(review, retryCount + 1)
        }
    }

    fun parseOcrText(ocrText: OcrTextRequest): ParsedOcrResponse {
        return runBlocking(GracefulShutdownDispatcher.dispatcher) {
            withTimeout(5000.milliseconds) {
                val shopNamePrompt = promptService.getByStyle(Style.OCR_SHOP_NAME)
                val itemNamePrompt = promptService.getByStyle(Style.OCR_ITEM_NAME)

                val shopNameOcrRequest = OpenAIOcrParser.Request.from(shopNamePrompt, ocrText.text)
                logger.info("ocr request $shopNameOcrRequest")

                val itemNameOcrRequest = OpenAIOcrParser.Request.from(itemNamePrompt, ocrText.text)
                logger.info("ocr request $itemNameOcrRequest")

                parseOcrWithRetry(shopNameOcrRequest, itemNameOcrRequest, 0)
            }
        }
    }

    private suspend fun parseOcrWithRetry(
        shopNameOcrRequest: OpenAIOcrParser.Request,
        itemNameOcrRequest: OpenAIOcrParser.Request,
        retryCount: Int,
    ): ParsedOcrResponse {
        return runCatching {
            val shopNameDeferred = CoroutineScope(GracefulShutdownDispatcher.dispatcher).async {
                parseShopName(shopNameOcrRequest)
            }
            val itemNameDeferred = CoroutineScope(GracefulShutdownDispatcher.dispatcher).async {
                parseItemNames(itemNameOcrRequest)
            }

            val shopNameKeyValuePair = shopNameDeferred.await()
            val itemNameKeyValuePairs = itemNameDeferred.await()

            val parsedOcr = ParsedOcrResponse(shopNameKeyValuePair + itemNameKeyValuePairs)

            require(parsedOcr.parsed.isNotEmpty()) { "Parsed OCR content is empty" }
            parsedOcr
        }.getOrElse {
            logger.error("OCR Parsing fail", it)
            if (it is IllegalArgumentException) {
                throw it
            }

            if (retryCount < MAX_RETRY_COUNT) {
                return@getOrElse parseOcrWithRetry(shopNameOcrRequest, itemNameOcrRequest, retryCount + 1)
            }
            throw it
        }
    }

    private fun parseItemNames(itemNameOcrRequest: OpenAIOcrParser.Request): List<ParsedOcrResponse.KeyValuePair> {
        val itemNameOcrResponse = openAiOcrParser.createParsedOcr(itemNameOcrRequest)
        logger.info("item name ocr response before parsing $itemNameOcrResponse")

        val itemNameResponseContent =
            itemNameOcrResponse.choices?.firstOrNull()?.message?.content ?: ""
        logger.info("item name ocr responseContent $itemNameResponseContent")

        if (isParsable(itemNameResponseContent)) return emptyList()

        val itemNameKeyValuePairs = itemNameResponseContent.split(',').map {
            ParsedOcrResponse.KeyValuePair(Style.OCR_ITEM_NAME.key, it.trim())
        }
        return itemNameKeyValuePairs
    }

    private fun parseShopName(shopNameOcrRequest: OpenAIOcrParser.Request): List<ParsedOcrResponse.KeyValuePair> {
        val shopNameOcrResponse = openAiOcrParser.createParsedOcr(shopNameOcrRequest)
        logger.info("shop name ocr response before parsing $shopNameOcrResponse")

        val shopNameResponseContent =
            shopNameOcrResponse.choices?.firstOrNull()?.message?.content ?: ""
        logger.info("shop name ocr responseContent $shopNameResponseContent")

        if (isParsable(shopNameResponseContent)) return emptyList()

        val shopNameKeyValuePair =
            ParsedOcrResponse.KeyValuePair(Style.OCR_SHOP_NAME.key, shopNameResponseContent)
        return listOf(shopNameKeyValuePair)
    }

    private fun isParsable(shopNameResponseContent: String): Boolean {
        return shopNameResponseContent.equals(UNPARSABLE, ignoreCase = true)
    }

    private companion object {
        private const val MAX_RETRY_COUNT = 3
        private const val ALREADY_COMPLETED = "stop_before"
        private const val UNPARSABLE = "X"
    }
}
