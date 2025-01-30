package me.misik.api.api

import me.misik.api.api.request.CreateReviewRequest
import me.misik.api.api.request.OcrTextRequest
import me.misik.api.api.response.ParsedOcrResponse
import me.misik.api.app.CreateReviewFacade
import me.misik.api.app.ReCreateReviewFacade
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val createReviewFacade: CreateReviewFacade,
    private val reCreateReviewFacade: ReCreateReviewFacade,
) {

    @PostMapping("reviews")
    fun createReview(
        @RequestHeader("device-id") deviceId: String,
        @RequestBody createReviewRequest: CreateReviewRequest,
    ) : Long = createReviewFacade.createReviewInBackground(deviceId, createReviewRequest)

    @PostMapping("reviews/{id}/re-create")
    fun reCreateReview(
        @RequestHeader("device-id") deviceId: String,
        @PathVariable("id") id: Long,
    ) = reCreateReviewFacade.reCreateReviewInBackground(deviceId, id)

    @PostMapping("reviews/ocr-parsing")
    fun parseOcrText(
        @RequestBody ocrText: OcrTextRequest,
    ) : ParsedOcrResponse = createReviewFacade.parseOcrText(ocrText)
}
