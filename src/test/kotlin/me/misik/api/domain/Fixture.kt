package me.misik.api.domain

import me.misik.api.domain.request.CreateReviewRequest

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
    style: Style = Style.PROFESSIONAL,
    text: String = "",
    hashTags: List<String> = listOf(),
): RequestPrompt = RequestPrompt(
    style = style,
    ocrText = text,
    promptCommand = text,
    hashTags = hashTags,
)

fun createReviewRequestFixture(
    ocrText: String = "testOcrText",
    hashTag: List<String> = listOf("testHashTag"),
    style: Style = Style.PROFESSIONAL
): CreateReviewRequest = CreateReviewRequest(
    ocrText = ocrText,
    hashTag = hashTag,
    reviewStyle = style,
)

fun deviceIdFixture(): String = "testDeviceId"

fun promptCommandFixture(): String = "testPromptCommand"