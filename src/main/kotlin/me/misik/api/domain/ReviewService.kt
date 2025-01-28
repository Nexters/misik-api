package me.misik.api.domain

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReviewService(
    private val reviewRepository: ReviewRepository,
) {

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

    fun getById(id: Long): Review = reviewRepository.findByIdOrNull(id)
        ?: throw IllegalArgumentException("Cannot find review by id \"$id\"")
}
