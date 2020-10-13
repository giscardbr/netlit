package netlit.payments.infra.utils

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object HttpRequest {

    val url: String
        get() {
            val attributes = RequestContextHolder.currentRequestAttributes() as? ServletRequestAttributes
            val requestURL = attributes?.request?.requestURL
            return requestURL?.toString() ?: ""
        }
}