package me.misik.api.domain.response

import me.misik.api.domain.ReviewStyle

data class ReviewStyleResponse(
    val icon: String,
    val style: String
) {
    companion object {
        fun from(reviewStyle: ReviewStyle): ReviewStyleResponse {
            return ReviewStyleResponse(
                icon = reviewStyle.iconUrl,
                style = reviewStyle.name,
            )
        }
    }
}
