package me.misik.api.app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.launch
import me.misik.api.core.Chatbot
import me.misik.api.core.GracefulShutdownDispatcher
import me.misik.api.domain.CreateReviewCache
import me.misik.api.domain.Review
import me.misik.api.domain.ReviewService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ReCreateReviewFacade(
    private val chatbot: Chatbot,
    private val reviewService: ReviewService,
    private val createReviewCache: CreateReviewCache
) {

    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    fun reCreateReviewInBackground(deviceId: String, id: Long) {
        reviewService.clearReview(id)

        val review = reviewService.getById(id)

        reCreateReviewWithRetry(review, retryCount = 0)
    }

    private fun reCreateReviewWithRetry(review: Review, retryCount: Int) {
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
            reCreateReviewWithRetry(review, retryCount + 1)
        }
    }

    private companion object {
        private const val MAX_RETRY_COUNT = 3
        private const val ALREADY_COMPLETED = "stop_before"
    }
}
