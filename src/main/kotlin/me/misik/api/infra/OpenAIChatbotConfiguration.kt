package me.misik.api.infra

import me.misik.api.core.OpenAIOcrParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class OpenAIChatbotConfiguration(
    @Value("\${me.misik.chatbot.openai.url:https://api.openai.com/}") private val chatbotUrl: String,
    @Value("\${me.misik.chatbot.openai.authorization}") private val authorization: String,
) {

    @Bean
    fun openAiOcrParser() : OpenAIOcrParser {
        val restClient = RestClient.builder()
            .baseUrl(chatbotUrl)
            .defaultHeaders { headers ->
                headers.add(HttpHeaders.AUTHORIZATION, "Bearer $authorization")
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }
            .build()

        val httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build()

        return httpServiceProxyFactory.createClient(OpenAIOcrParser::class.java)
    }
}