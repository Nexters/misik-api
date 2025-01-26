package me.misik.api.api

import me.misik.api.app.CreateReviewFacade
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val createReviewFacade: CreateReviewFacade,
) {

    @PostMapping("reviews/{id}/re-create")
    fun createReview(
        @PathVariable("id") id: Long,
    ) = createReviewFacade.reCreateReviewInBackground(id)
}
