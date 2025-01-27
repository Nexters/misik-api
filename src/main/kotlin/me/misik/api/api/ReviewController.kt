package me.misik.api.api

import me.misik.api.api.response.ReviewResponse
import me.misik.api.app.CreateReviewFacade
import me.misik.api.app.GetReviewFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val createReviewFacade: CreateReviewFacade,
    private val getReviewFacade: GetReviewFacade,
) {

    @PostMapping("reviews/{id}/re-create")
    fun createReview(
        @PathVariable("id") id: Long,
    ) = createReviewFacade.reCreateReviewInBackground(id)

    @GetMapping("reviews/{id}")
    fun getReview(
        @PathVariable("id") id: Long,
    ): ReviewResponse = ReviewResponse.of(getReviewFacade.getReview(id))
}
