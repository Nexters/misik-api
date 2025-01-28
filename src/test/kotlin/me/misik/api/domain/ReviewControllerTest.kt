package me.misik.api.domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.misik.api.api.ReviewController
import me.misik.api.api.response.ReviewStylesResponse

class ReviewControllerTest : DescribeSpec({

    val reviewStyleService = mockk<ReviewStyleService>()
    val reviewController = ReviewController(mockk(), mockk(), reviewStyleService)

    describe("ReviewController 클래스의") {
        describe("getReviewStyles 메소드는") {
            context("reviewStyleService에서 모든 ReviewStyle를 받아") {
                it("ReviewStylesResponse를 올바르게 반환 한다") {
                    // Given
                    val reviewStyles = ReviewStyle.values()
                    every { reviewStyleService.getAll() } returns reviewStyles

                    // When
                    val result = reviewController.getReviewStyles()

                    // Then
                    val expectedResponse = ReviewStylesResponse.from(reviewStyles)
                    result shouldBe expectedResponse
                    verify { reviewStyleService.getAll() }
                }
            }
        }
    }
})
