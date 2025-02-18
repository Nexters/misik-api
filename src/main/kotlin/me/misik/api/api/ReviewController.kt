package me.misik.api.api

import me.misik.api.api.response.ReviewResponse
import me.misik.api.api.response.ParsedOcrResponse
import me.misik.api.app.CreateReviewFacade
import me.misik.api.app.ReCreateReviewFacade
import me.misik.api.app.GetReviewFacade
import me.misik.api.domain.request.CreateReviewRequest
import me.misik.api.domain.request.OcrTextRequest
import me.misik.api.domain.Style
import me.misik.api.domain.response.ReviewStylesResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val createReviewFacade: CreateReviewFacade,
    private val getReviewFacade: GetReviewFacade,
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

    @GetMapping("reviews/styles")
    fun getReviewStyles() : ReviewStylesResponse {
        val styles = Style.entries.toList()

        return ReviewStylesResponse.from(styles)
    }

    @GetMapping("reviews/{id}")
    fun getReview(
        @PathVariable("id") id: Long,
    ): ReviewResponse = ReviewResponse.of(getReviewFacade.getReview(id))

    @PostMapping("reviews/ocr-parsing")
    fun parseOcrText(
        @RequestBody ocrText: OcrTextRequest,
    ) : ParsedOcrResponse = createReviewFacade.parseOcrText(ocrText)
}
