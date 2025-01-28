package me.misik.api.domain

import me.misik.api.api.request.CreateReviewRequest

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
    style: ReviewStyle = ReviewStyle.PROFESSIONAL,
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
    reviewStyle: ReviewStyle = ReviewStyle.PROFESSIONAL
): CreateReviewRequest = CreateReviewRequest(
    ocrText = ocrText,
    hashTag = hashTag,
    reviewStyle = reviewStyle,
)

fun deviceIdFixture(): String = "testDeviceId"

fun promptCommandFixture(): String = "testPromptCommand"