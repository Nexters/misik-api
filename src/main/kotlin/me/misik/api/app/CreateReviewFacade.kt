package me.misik.api.app;

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.launch
import me.misik.api.api.request.CreateReviewRequest
import me.misik.api.core.Chatbot
import me.misik.api.core.GracefulShutdownDispatcher
import me.misik.api.domain.CreateReviewCache
import me.misik.api.domain.Review
import me.misik.api.domain.ReviewService
import me.misik.api.domain.query.PromptService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class CreateReviewFacade(
    private val chatbot:Chatbot,
    private val reviewService:ReviewService,
    private val promptService: PromptService,
    private val createReviewCache: CreateReviewCache
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

    private companion object {
        private const val MAX_RETRY_COUNT = 3
        private const val ALREADY_COMPLETED = "stop_before"
    }
}
