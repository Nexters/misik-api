package me.misik.api.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReviewService(
    private val reviewRepository: ReviewRepository,
) {
}
