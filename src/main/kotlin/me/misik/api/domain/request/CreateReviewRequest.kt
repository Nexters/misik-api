package me.misik.api.domain.request

import me.misik.api.domain.Style

data class CreateReviewRequest(
    val ocrText: String,
    val hashTag: List<String>,
    val reviewStyle: Style = Style.FRIENDLY,
)
