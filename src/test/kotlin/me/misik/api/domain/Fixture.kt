package me.misik.api.domain

fun review(
    id: Long = 0L,
    isCompleted: Boolean = false,
    text: String = "",
    deviceId: String = "",
    requestPrompt: RequestPrompt = requestPrompt()
): Review = Review(
    id = id,
    isCompleted = isCompleted,
    text = text,
    deviceId = deviceId,
    requestPrompt = requestPrompt,
)

fun requestPrompt(
    style: ReviewStyle = ReviewStyle.DUMMY,
    text: String = "",
    hashTags: List<String> = listOf(),
): RequestPrompt = RequestPrompt(
    style = style,
    text = text,
    hashTags = hashTags,
)
