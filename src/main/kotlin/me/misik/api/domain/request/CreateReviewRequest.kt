package me.misik.api.domain.request

import me.misik.api.domain.ReviewStyle

data class CreateReviewRequest(
    val ocrText: String,
    val hashTag: List<String>,
    val reviewStyle: ReviewStyle = ReviewStyle.FRIENDLY,
)
