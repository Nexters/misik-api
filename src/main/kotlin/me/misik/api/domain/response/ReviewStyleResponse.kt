package me.misik.api.domain.response

import me.misik.api.domain.Style

data class ReviewStyleResponse(
    val icon: String,
    val style: String
) {
    companion object {
        fun from(style: Style): ReviewStyleResponse {
            return ReviewStyleResponse(
                icon = style.iconUrl,
                style = style.name,
            )
        }
    }
}
