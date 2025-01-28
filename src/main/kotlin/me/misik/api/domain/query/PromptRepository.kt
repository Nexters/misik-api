package me.misik.api.domain.query

import me.misik.api.domain.ReviewStyle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PromptRepository : JpaRepository<Prompt, Long> {
    @Query("SELECT p FROM Prompt p WHERE (:reviewStyle IS NULL OR p.style = :reviewStyle)")
    fun findByReviewStyleOrNull(@Param("reviewStyle") reviewStyle: ReviewStyle?): Prompt?
}