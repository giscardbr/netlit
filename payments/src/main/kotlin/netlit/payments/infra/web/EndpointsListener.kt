package netlit.payments.infra.web

import org.apache.logging.log4j.LogManager
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

@Component
class EndpointsListener {

    private val log = LogManager.getLogger(EndpointsListener::class.java)

    @EventListener
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        val context = event.applicationContext
        val bean = context.getBean(RequestMappingHandlerMapping::class.java)
        val handlerMethods = bean.handlerMethods
        handlerMethods.forEach { (mapping, _) -> log.info(mapping) }
    }
}
