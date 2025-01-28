package me.misik.api.api

import me.misik.api.app.ReCreateReviewFacade
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val reCreateReviewFacade: ReCreateReviewFacade,
) {

    @PostMapping("reviews/{id}/re-create")
    fun reCreateReview(
        @PathVariable("id") id: Long,
    ) = reCreateReviewFacade.reCreateReviewInBackground(id)
}
