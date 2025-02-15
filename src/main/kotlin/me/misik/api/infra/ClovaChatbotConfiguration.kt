package me.misik.api.infra

import me.misik.api.core.Chatbot
import me.misik.api.core.OcrParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import kotlin.time.Duration.Companion.milliseconds


@Configuration
class ClovaChatbotConfiguration(
    @Value("\${me.misik.chatbot.clova.url:https://clovastudio.stream.ntruss.com/}") private val chatbotUrl: String,
    @Value("\${me.misik.chatbot.clova.authorization}") private val authorization: String,
) {

    @Bean
    fun clovaChatbot(): Chatbot {
        val webClient = WebClient.builder()
            .baseUrl(chatbotUrl)
            .defaultHeaders { headers ->
                headers.add(HttpHeaders.AUTHORIZATION, "Bearer $authorization")
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                headers.add(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
            }
            .build()

        val httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build()

        return httpServiceProxyFactory.createClient(Chatbot::class.java)
    }

    @Bean
    fun ocrParser(): OcrParser {
        val clientHttpRequestFactory = HttpComponentsClientHttpRequestFactory()
        clientHttpRequestFactory.setReadTimeout(60000)

        val restClient = RestClient.builder()
            .baseUrl(chatbotUrl)
            .defaultHeaders { headers ->
                headers.add(HttpHeaders.AUTHORIZATION, "Bearer $authorization")
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }
            .requestFactory(clientHttpRequestFactory)
            .build()

        val httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build()

        return httpServiceProxyFactory.createClient(OcrParser::class.java)
    }
}
