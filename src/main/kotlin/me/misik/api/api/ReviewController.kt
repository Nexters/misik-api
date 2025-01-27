package me.misik.api.api

import me.misik.api.api.request.CreateReviewRequest
import me.misik.api.api.response.ReviewResponse
import me.misik.api.app.CreateReviewFacade
import me.misik.api.app.GetReviewFacade
import org.springframework.web.bind.annotation.*

@RestController
class ReviewController(
    private val createReviewFacade: CreateReviewFacade,
    private val getReviewFacade: GetReviewFacade,
) {

    @PostMapping("reviews")
    fun createReview(
        @RequestHeader("device-id") deviceId: String,
        @RequestBody createReviewRequest: CreateReviewRequest,
    ) : Long = createReviewFacade.createReviewInBackground(deviceId, createReviewRequest)

    @PostMapping("reviews/{id}/re-create")
    fun createReview(
        @RequestHeader("device-id") deviceId: String,
        @PathVariable("id") id: Long,
    ) = createReviewFacade.reCreateReviewInBackground(id, deviceId)

    @GetMapping("reviews/{id}")
    fun getReview(
        @PathVariable("id") id: Long,
    ): ReviewResponse = ReviewResponse.of(getReviewFacade.getReview(id))
}
