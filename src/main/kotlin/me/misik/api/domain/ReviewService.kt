package me.misik.api.domain

import me.misik.api.api.request.CreateReviewRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReviewService(
    private val reviewRepository: ReviewRepository,
) {

    @Transactional
    fun createReview(deviceId: String, createReviewRequest: CreateReviewRequest): Review {
        val requestPrompt = RequestPrompt(
            style = createReviewRequest.reviewStyle,
            text = createReviewRequest.ocrText,
            hashTags = createReviewRequest.hashTag
        )

        val review = Review.create(
            deviceId = deviceId,
            requestPrompt = requestPrompt
        )

        return reviewRepository.save(review)
    }

    @Transactional
    fun setReviewCompletedStatus(id: Long, completedStatus: Boolean) =
        reviewRepository.setReviewCompletedStatus(id, completedStatus)

    @Transactional
    fun updateReview(id: Long, text: String) = reviewRepository.addText(id, text)

    fun getReview(id: Long) = reviewRepository.findById(id)
        ?: throw IllegalArgumentException("Cannot find review by id \"$id\"")
}
