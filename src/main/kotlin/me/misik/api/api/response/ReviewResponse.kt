package me.misik.api.api.response

import me.misik.api.domain.Review

data class ReviewResponse(
    val isSuccess: Boolean,
    val id: String,
    val review: String,
) {

    companion object {
        fun of(review: Review): ReviewResponse = ReviewResponse(
            isSuccess = review.isCompleted,
            id = review.id.toString(),
            review = review.text,
        )
    }
}
