package me.misik.api.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReviewService(
    private val reviewRepository: ReviewRepository,
) {

    @Transactional
    fun setReviewCompletedStatus(id: Long, completedStatus: Boolean) =
        reviewRepository.setReviewCompletedStatus(id, completedStatus)

    @Transactional
    fun updateReview(id: Long, text: String) = reviewRepository.addText(id, text)

    fun getReview(id: Long) = reviewRepository.findById(id)
        ?: throw IllegalArgumentException("Cannot find review by id \"$id\"")
}
