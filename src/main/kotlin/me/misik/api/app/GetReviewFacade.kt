package me.misik.api.app

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import me.misik.api.core.GracefulShutdownDispatcher
import me.misik.api.domain.Review
import me.misik.api.domain.ReviewService
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.seconds

@Component
class GetReviewFacade(
    private val reviewService: ReviewService,
) {

    fun getReview(id: Long): Review {
        return runBlocking(GracefulShutdownDispatcher.dispatcher) {
            withTimeout(60.seconds) {
                var result: Review? = null
                while (result == null) {
                    reviewService.getById(id)
                        .takeIf { it.isCompleted }
                        .let {
                            result = it
                        }
                }
                return@withTimeout result!!
            }
        }
    }
}
