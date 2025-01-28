package me.misik.api.domain

import io.kotest.common.runBlocking
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.launch
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [ReviewService::class])
@EntityScan(basePackages = ["me.misik.api.domain"])
@EnableJpaRepositories(basePackages = ["me.misik.api.domain"])
@DisplayName("ReviewService 클래스의")
class ReviewServiceTest(
    private val reviewService: ReviewService,
    private val reviewRepository: ReviewRepository,
) : DescribeSpec({
    afterEach {
        reviewRepository.deleteAllInBatch()
    }

    describe("completeReview 메소드는") {

        context("id를 입력받으면,") {
            val review = reviewRepository.save(review(isCompleted = false))

            it("review의 isCompleted 상태를 true로 변경한다") {
                reviewService.completeReview(review.id)

                reviewRepository.getReferenceById(review.id).isCompleted shouldBe true
            }
        }
    }

    describe("updateReview 메소드는") {
        context("id와 text를 입력받으면,") {
            val review = reviewRepository.save(review(text = "A"))
            it("기존 text에 새로운 text를 순차적으로 추가한다") {
                runBlocking {
                    launch {
                        reviewService.updateReview(review.id, "B")
                    }
                    launch {
                        reviewService.updateReview(review.id, "C")
                    }
                    launch {
                        reviewService.updateReview(review.id, "D")
                    }
                }.invokeOnCompletion {
                    reviewRepository.getReferenceById(review.id).text shouldBe "ABCD"
                }

            }
        }
    }
})
