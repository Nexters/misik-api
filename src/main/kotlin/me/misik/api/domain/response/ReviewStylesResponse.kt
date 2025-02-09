package me.misik.api.domain.response

import me.misik.api.domain.ReviewStyle

data class ReviewStylesResponse(
    val reviewStyles: List<ReviewStyleResponse>
) {
    companion object {
        fun from(reviewStyles: List<ReviewStyle>) : ReviewStylesResponse {
            return ReviewStylesResponse(
                reviewStyles.map { ReviewStyleResponse.from(it) }
            )
        }
    }
}