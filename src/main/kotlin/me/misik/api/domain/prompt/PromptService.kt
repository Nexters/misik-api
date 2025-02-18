package me.misik.api.domain.prompt

import me.misik.api.domain.Style
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PromptService(
    private val promptRepository: PromptRepository,
) {

    fun getByStyle(style: Style): Prompt = promptRepository.findByReviewStyleOrNull(style)
        ?: throw IllegalArgumentException("Cannot find prompt by review style \"$style\"")

    fun findAllByType(promptType: PromptType): List<Prompt> = promptRepository.findAllByType(promptType)
}
