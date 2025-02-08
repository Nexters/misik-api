package me.misik.api.infra

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        val info = Info()
            .title("Misik API")
            .version("1.0")
            .description("Misik API Description")

        val server = Server()
        server.url = SWAGGER_URL

        return OpenAPI()
            .info(info)
            .servers(listOf(server))
    }

    private companion object {
        private const val SWAGGER_URL = "misik-api"
    }
}
