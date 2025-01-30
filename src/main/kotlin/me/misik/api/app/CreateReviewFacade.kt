package me.misik.api.app;

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.launch
import me.misik.api.api.request.CreateReviewRequest
import me.misik.api.api.request.OcrTextRequest
import me.misik.api.api.response.ParsedOcrResponse
import me.misik.api.core.Chatbot
import me.misik.api.core.GracefulShutdownDispatcher
import me.misik.api.domain.CreateReviewCache
import me.misik.api.domain.Review
import me.misik.api.domain.ReviewService
import me.misik.api.domain.query.PromptService
import me.misik.api.core.OcrParser
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class CreateReviewFacade(
    private val chatbot:Chatbot,
    private val ocrParser: OcrParser,
    private val reviewService:ReviewService,
    private val promptService: PromptService,
    private val createReviewCache: CreateReviewCache,
    private val objectMapper: ObjectMapper,
) {

    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    fun createReviewInBackground(deviceId:String, createReviewRequest: CreateReviewRequest) : Long {
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
                    review.text = review.text + newText

                    val updatedReview = review.copy()
                    createReviewCache.put(review.id, updatedReview)
                }
        }.invokeOnCompletion {
            if (it == null) {
                createReviewCache.get(review.id)?.let {
                    reviewService.updateAndCompleteReview(it.id, it.text)
                }
                createReviewCache.remove(review.id)
                return@invokeOnCompletion
            }
            if (retryCount == MAX_RETRY_COUNT) {
                logger.error("Failed to create review.", it)
                throw it
            }
            logger.warn("Failed to create review. retrying... retryCount: \"${retryCount + 1}\"", it)
            createReviewWithRetry(review, retryCount + 1)
        }
    }

    fun parseOcrText( ocrText: OcrTextRequest): ParsedOcrResponse {
        val response = ocrParser.createParsedOcr(OcrParser.Request.from(ocrText.text))
        val responseContent = response.result?.message?.content?: ""

        try {
            val parsedOcr: ParsedOcrResponse = objectMapper.readValue(
                responseContent,
                ParsedOcrResponse::class.java
            )

            if (parsedOcr.parsed.isEmpty()) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            }

            return parsedOcr
        } catch (e: Exception) {
            logger.error("Failed to parse ocr text.", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private companion object {
        private const val MAX_RETRY_COUNT = 3
        private const val ALREADY_COMPLETED = "stop_before"
    }
}
