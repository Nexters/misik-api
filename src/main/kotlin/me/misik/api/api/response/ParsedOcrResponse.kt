package me.misik.api.api.response

data class ParsedOcrResponse(
    val parsed: List<KeyValuePair>
) {
    data class KeyValuePair(
        val key: String,
        val value: String
    )
}