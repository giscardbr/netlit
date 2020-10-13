package netlit.payments.infra.web

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ClientConfig {

    @Bean
    fun restTemplate() = RestTemplateBuilder().build()!!
}
