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
            val cachedParsingSystemMessage = Message.createSystem(
                """
                    리뷰에 쓸만한 정보를 추출해줘. key에는 방문 장소명, 품명 등이 포함될 수 있어. key는 최대 3개만 뽑아줘. 
                    응답 형식은 반드시 다음과 같은 JSON이야. 응답에는 해당 JSON만 있어야해. 
                    {
                      "parsed": [
                        {
                          "key": "품명",
                          "value": "카야토스트+음료세트"
                        },
                        {
                          "key": "가격",
                          "value": "카야토스트+음료세트"
                        },
                        ...
                      ]
                    }
                   응답의 총 길이는 300자를 넘으면 안돼.
                """
            )

            fun from(review: Review): Request {
                return Request(
                    messages = listOf(
                        Message.createSystem(review.requestPrompt.promptCommand + review.requestPrompt.hashTags.joinToString(", ") { "$it" }),
                        Message.createUser(review.requestPrompt.ocrText)
                    )
                )
            }

            fun createParsedOcr(ocrText: String): Request {
                return Request(
                    messages = listOf(
                        cachedParsingSystemMessage,
                        Message.createUser(ocrText)
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

