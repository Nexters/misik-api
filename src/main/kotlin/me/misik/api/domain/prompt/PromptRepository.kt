package me.misik.api.domain.prompt

import me.misik.api.domain.ReviewStyle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PromptRepository : JpaRepository<Prompt, Long> {
    @Query("SELECT p FROM Prompt p WHERE p.style = :reviewStyle")
    fun findByReviewStyleOrNull(@Param("reviewStyle") reviewStyle: ReviewStyle?): Prompt?

    @Query("SELECt p FROM Prompt p WHERE p.type = :promptType")
    fun findAllByType(@Param("promptType") promptType: PromptType): List<Prompt>
}
