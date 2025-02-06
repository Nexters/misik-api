package me.misik.api.core

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
                          "value": "3000"
                        },
                        ...
                      ]
                    }
                   응답의 총 길이는 300자를 넘으면 안돼.
                """
            )

            fun from(ocrText: String): Request {
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