package me.misik.api.domain

import me.misik.api.domain.request.CreateReviewRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReviewService(
    private val reviewRepository: ReviewRepository,
) {

    @Transactional
    fun createReview(deviceId: String, promptCommand: String, createReviewRequest: CreateReviewRequest): Review {
        val requestPrompt = RequestPrompt(
            style = createReviewRequest.reviewStyle,
            ocrText = createReviewRequest.ocrText,
            promptCommand = promptCommand,
            hashTags = createReviewRequest.hashTag
        )

        val review = Review.create(
            deviceId = deviceId,
            requestPrompt = requestPrompt
        )

        return reviewRepository.save(review)
    }

    @Transactional
    fun clearReview(id: Long) {
        val review = reviewRepository.getReviewWithReadLock(id)
        review.text = ""
        review.isCompleted = false
    }

    @Transactional
    fun completeReview(id: Long) = reviewRepository.setReviewCompletedStatus(id, true)

    @Transactional
    fun updateReview(id: Long, text: String) = reviewRepository.addText(id, text)

    @Transactional
    fun updateAndCompleteReview(id: Long, text: String) = reviewRepository.updateTextAndComplete(id, text)

    fun getById(id: Long): Review = reviewRepository.findByIdOrNull(id)
        ?: throw IllegalArgumentException("Cannot find review by id \"$id\"")


    fun getReview(id: Long) = reviewRepository.findById(id)
        ?: throw IllegalArgumentException("Cannot find review by id \"$id\"")
}
