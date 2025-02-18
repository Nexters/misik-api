package me.misik.api.core

import me.misik.api.domain.prompt.Prompt
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange

fun interface OpenAIOcrParser {

    @PostExchange("/v1/chat/completions")
    fun createParsedOcr(@RequestBody request: Request): Response

    data class Request(
        val model: String = "gpt-4o-mini",
        val messages: List<Message>,
        val max_tokens: Int = 1000,
    ) {
        data class Message(
            val role: String,
            val content: String
        ) {
            companion object {
                fun createSystem(content: String) = Message(
                    role = "system",
                    content = content,
                )

                fun createUser(content: String) = Message(
                    role = "user",
                    content = content,
                )
            }
        }

        companion object {
            fun from(prompt: Prompt, ocrText: String): Request {
                return Request(
                    messages = listOf(
                        Message.createSystem(prompt.command),
                        Message.createUser(ocrText),
                    )
                )
            }
        }
    }

    data class Response(
        val id: String?,
        val created: Long?,
        val model: String?,
        val choices: List<Choice>?
    ) {
        data class Choice(
            val message: Message,
            val finish_reason: String
        )

        data class Message(
            val role: String,
            val content: String
        )
    }

}
