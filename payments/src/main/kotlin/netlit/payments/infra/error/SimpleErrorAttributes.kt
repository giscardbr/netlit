package netlit.payments.infra.error

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest

@Component
class SimpleErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(webRequest: WebRequest, includeStackTrace: Boolean): Map<String, Any> {
        val errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace)
        errorAttributes.remove("path")
        errorAttributes.remove("status")
        errorAttributes.remove("timestamp")
        return errorAttributes
    }
}