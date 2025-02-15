package me.misik.api.domain.prompt

import me.misik.api.domain.ReviewStyle
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PromptService(
    private val promptRepository: PromptRepository,
) {

    fun getByStyle(reviewStyle: ReviewStyle): Prompt = promptRepository.findByReviewStyleOrNull(reviewStyle)
        ?: throw IllegalArgumentException("Cannot find prompt by review style \"$reviewStyle\"")

    fun findAllByType(promptType: PromptType): List<Prompt> = promptRepository.findAllByType(promptType)
}
