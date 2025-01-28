package me.misik.api.core

import kotlinx.coroutines.flow.Flow
import me.misik.api.domain.Review
import me.misik.api.domain.query.Prompt
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange

fun interface Chatbot {

    @PostExchange("/testapp/v1/chat-completions/HCX-003")
    fun createReviewWithModelName(@RequestBody request: Request): Flow<Response>

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

            fun from(review: Review): Request {
                return Request(
                    messages = listOf(
                        Message.createSystem(review.requestPrompt.promptCommand),
                        Message.createUser(review.requestPrompt.ocrText)
                    )
                )
            }
        }
    }

    data class Response(
        val stopReason: String?,
        val message: Message?,
    ) {
        data class Message(
            val role: String,
            val content: String,
        )
    }
}
