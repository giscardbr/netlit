package netlit.payments.infra.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.CommonsRequestLoggingFilter
import javax.servlet.http.HttpServletRequest

@Component
class RequestLoggingFilter(@param:Value("\${server.servlet.context-path}") val path: String) : CommonsRequestLoggingFilter() {

    init {
        this.isIncludeQueryString = true
        this.isIncludePayload = false
        this.isIncludeHeaders = false
    }

    override fun shouldLog(request: HttpServletRequest): Boolean {
        return !request.requestURI.startsWith("${this.path}/actuator") && this.logger.isDebugEnabled
    }

    override fun createMessage(request: HttpServletRequest, prefix: String, suffix: String): String {
        val msg = StringBuilder()
        msg.append(prefix)
        msg.append(request.method).append(" ").append(request.requestURI)

        if (this.isIncludeQueryString) {
            val queryString = request.queryString
            if (queryString != null) {
                msg.append('?').append(queryString)
            }
        }

        if (this.isIncludeClientInfo) {
            val client = request.remoteAddr
            if (StringUtils.hasLength(client)) {
                msg.append(";client=").append(client)
            }
            val session = request.getSession(false)
            if (session != null) {
                msg.append(";session=").append(session.id)
            }
            val user = request.remoteUser
            if (user != null) {
                msg.append(";user=").append(user)
            }
        }

        if (this.isIncludeHeaders) {
            msg.append(";headers=").append(ServletServerHttpRequest(request).headers)
        }

        if (this.isIncludePayload) {
            val payload = this.getMessagePayload(request)
            if (payload != null) {
                msg.append(";payload=").append(payload)
            }
        }

        msg.append(suffix)
        return msg.toString()
    }

}
