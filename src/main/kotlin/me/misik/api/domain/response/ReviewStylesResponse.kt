package me.misik.api.domain.response

import me.misik.api.domain.Style

data class ReviewStylesResponse(
    val reviewStyles: List<ReviewStyleResponse>
) {
    companion object {
        fun from(styles: List<Style>): ReviewStylesResponse {
            return ReviewStylesResponse(
                styles.filterNot { it == Style.OCR_ITEM_NAME || it == Style.OCR_SHOP_NAME }
                    .map { ReviewStyleResponse.from(it) }
            )
        }
    }
}
