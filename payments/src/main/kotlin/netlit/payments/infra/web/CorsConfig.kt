package netlit.payments.infra.web

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): FilterRegistrationBean<CorsFilter> {
        val configuration = CorsConfiguration()
        configuration.allowCredentials = true
        configuration.addAllowedOrigin("*")
        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        val registrationBean = FilterRegistrationBean(CorsFilter(source))
        registrationBean.order = Ordered.HIGHEST_PRECEDENCE

        return registrationBean
    }
}