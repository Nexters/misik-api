package me.misik.api.app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.launch
import me.misik.api.core.Chatbot
import me.misik.api.core.GracefulShutdownDispatcher
import me.misik.api.domain.ReviewService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ReCreateReviewFacade(
    private val chatbot: Chatbot,
    private val reviewService: ReviewService,
) {

    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    fun reCreateReviewInBackground(id: Long) {
        reviewService.clearReview(id)

        reCreateReviewWithRetry(id, retryCount = 0)
    }

    private fun reCreateReviewWithRetry(id: Long, retryCount: Int) {
        CoroutineScope(GracefulShutdownDispatcher.dispatcher).launch {
            val review = reviewService.getById(id)

            chatbot.createReviewWithModelName(Chatbot.Request.from(review))
                .filterNot { it.stopReason == ALREADY_COMPLETED }
                .collect {
                    reviewService.updateReview(id, it.message?.content ?: "")
                }
        }.invokeOnCompletion {
            if (it == null) {
                return@invokeOnCompletion reviewService.completeReview(id)
            }
            if (retryCount == MAX_RETRY_COUNT) {
                logger.error("Failed to create review.", it)
                throw it
            }
            logger.warn("Failed to create review. retrying... retryCount: \"${retryCount + 1}\"", it)
            reCreateReviewWithRetry(id, retryCount + 1)
        }
    }

    private companion object {
        private const val MAX_RETRY_COUNT = 3
        private const val ALREADY_COMPLETED = "stop_before"
    }
}
