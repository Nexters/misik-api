package me.misik.api.core

import kotlinx.coroutines.flow.Flow
import me.misik.api.domain.Review
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange

fun interface Chatbot {

    @PostExchange("/testapp/v1/chat-completions/HCX-003")
    fun createReviewWithModelName(@RequestBody request: Request): Flow<Response>

    data class Request(
        val messages: List<Message>,
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
            private val cachedSystemMessage = Message.createSystem(
                """
                    너는 지금부터 음식에 대한 리뷰를 하는 고독한 미식가야.
                    답변에는 리뷰에 대한 내용만 포함해. 
                    또한, 응답 메시지는 공백을 포함해서 300자가 넘으면 절대로 안돼.
                """
            )

            fun from(review: Review): Request {
                return Request(
                    messages = listOf(
                        cachedSystemMessage,
                        Message.createUser(review.requestPrompt.text)
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
