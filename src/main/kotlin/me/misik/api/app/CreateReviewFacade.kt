package me.misik.api.app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.misik.api.api.request.CreateReviewRequest
import me.misik.api.core.Chatbot
import me.misik.api.core.GracefulShutdownDispatcher
import me.misik.api.domain.Review
import me.misik.api.domain.ReviewService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateReviewFacade(
    private val chatbot: Chatbot,
    private val reviewService: ReviewService,
) {

    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    fun createReviewInBackground(deviceId: String, createReviewRequest: CreateReviewRequest) : Long {
        val review = reviewService.createReview(deviceId, createReviewRequest)

        createReviewWithRetry(review, retryCount = 0)

        return review.id
    }

    private fun createReviewWithRetry(review: Review, retryCount: Int) {
        CoroutineScope(GracefulShutdownDispatcher.dispatcher).launch {
            chatbot.createReviewWithModelName("HCX-003")
                .collect { reviewService.updateReview(review.id, it) }
        }.invokeOnCompletion {
            if (it == null) {
                reviewService.setReviewCompletedStatus(review.id, true)
            }
            if (retryCount == MAX_RETRY_COUNT) {
                logger.error("Failed to create review.", it)
                throw it!!
            }
            logger.warn("Failed to create review. retrying... retryCount: \"${retryCount + 1}\"", it)
            createReviewWithRetry(review, retryCount + 1)
        }
    }

    fun reCreateReviewInBackground(id: Long, deviceId: String) {
        reviewService.setReviewCompletedStatus(id, false)

        reCreateReviewWithRetry(id, retryCount = 0)
    }

    private fun reCreateReviewWithRetry(id: Long, retryCount: Int) {
        CoroutineScope(GracefulShutdownDispatcher.dispatcher).launch {
            chatbot.createReviewWithModelName("HCX-003")
                .collect { reviewService.updateReview(id, it) }
        }.invokeOnCompletion {
            if (it == null) {
                reviewService.setReviewCompletedStatus(id, true)
            }
            if (retryCount == MAX_RETRY_COUNT) {
                logger.error("Failed to create review.", it)
                throw it!!
            }
            logger.warn("Failed to create review. retrying... retryCount: \"${retryCount + 1}\"", it)
            reCreateReviewWithRetry(id, retryCount + 1)
        }
    }

    private companion object {
        private const val MAX_RETRY_COUNT = 3
    }
}
