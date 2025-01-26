package me.misik.api.core

import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.PostExchange
import reactor.core.publisher.Flux

interface Chatbot {

    @PostExchange("/v1/chat-completions/{modelName}")
    fun createReviewWithModelName(@PathVariable("modelName") modelName: String): Flow<String>

    @PostExchange("/v2/tasks/{taskId}/chat-completions")
    fun createReviewWithTuning(@PathVariable("taskId") taskId: String): Flow<String>
}
