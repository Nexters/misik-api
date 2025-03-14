package me.misik.api.domain.prompt

import me.misik.api.domain.Style
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PromptRepository : JpaRepository<Prompt, Long> {
    @Query("SELECT p FROM Prompt p WHERE p.style = :style")
    fun findByReviewStyleOrNull(@Param("style") style: Style?): Prompt?

    @Query("SELECt p FROM Prompt p WHERE p.type = :promptType")
    fun findAllByType(@Param("promptType") promptType: PromptType): List<Prompt>
}
