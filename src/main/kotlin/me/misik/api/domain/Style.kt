package me.misik.api.domain

enum class Style(val iconUrl: String, val key: String) {
    PROFESSIONAL("https://kr.object.ncloudstorage.com/misik/review-style/professional-icon.png", "NOT_USE"),
    FRIENDLY("https://kr.object.ncloudstorage.com/misik/review-style/friendly-icon.png", "NOT_USE"),
    CUTE("https://kr.object.ncloudstorage.com/misik/review-style/cute-icon.png", "NOT_USE"),
    OCR_SHOP_NAME("NOT_USE","가게명"),
    OCR_ITEM_NAME("NOT_USE","품명"),
    ;
}
