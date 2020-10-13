package netlit.payments.infra.web

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ForwardedHeaderFilter

@Configuration
class HttpsConfig {

    @Bean
    fun forwardedHeaderFilter(): FilterRegistrationBean<ForwardedHeaderFilter> {
        val bean = FilterRegistrationBean<ForwardedHeaderFilter>()
        bean.filter = ForwardedHeaderFilter()
        return bean
    }
}
