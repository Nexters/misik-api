package me.misik.api.core

import me.misik.api.domain.prompt.Prompt
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange

fun interface OcrParser {

    @PostExchange("/testapp/v1/chat-completions/HCX-003")
    fun createParsedOcr(@RequestBody request: Request): Response

    data class Request(
        val messages: List<Message>,
        val maxTokens: Int = 100,
        val includeAiFilters: Boolean = true,
    ) {
        data class Message(
            val role: String,
            val content: String,
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

            fun of(prompt: Prompt, ocrText: String): Request {
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
        val status: Status?,
        val result: Result?
    ) {
        data class Status(
            val code: String,
            val message: String
        )
        data class Result(
            val message: Message?
        ) {
            data class Message(
                val role: String,
                val content: String
            )
        }
    }
}
