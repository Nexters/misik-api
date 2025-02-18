package me.misik.api.api.response

data class ParsedOcrResponse(
    val parsed: List<KeyValuePair>,
) {
    data class KeyValuePair(
        val key: String,
        val value: String,
    )

    companion object {
        fun from(parsed: List<KeyValuePair>) : ParsedOcrResponse {
            return ParsedOcrResponse(
                parsed
            )
        }

        fun from(key: String, value: String) : ParsedOcrResponse {
            return ParsedOcrResponse(
                listOf(KeyValuePair(key, value))
            )
        }
    }
}
