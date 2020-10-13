package netlit.payments.domain.credit_card.adyen

import netlit.payments.domain.credit_card.adyen.authorise.Authorization
import netlit.payments.domain.credit_card.adyen.authorise.AuthorizationRequest
import netlit.payments.domain.credit_card.adyen.payments.Amount
import netlit.payments.domain.credit_card.adyen.payments.PaymentMethod
import netlit.payments.domain.credit_card.adyen.payments.PaymentRequest
import netlit.payments.domain.credit_card.adyen.recurring_details.RecurringDetail
import netlit.payments.domain.credit_card.adyen.recurring_details.RecurringDetails
import netlit.payments.domain.credit_card.adyen.recurring_details.RecurringDetailsRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@Component
class Adyen(
        @Value("\${adyen.api-key}")
        val apiKey: String,
        @Value("\${adyen.endpoint}")
        val endpoint: String,
        @Value("\${adyen.merchant-account}")
        val merchantAccount: String,
        @Value("\${adyen.currency}")
        val currency: String,
        val restTemplate: RestTemplate) {

    fun authorize(request: AuthorizationRequest) {
        request.merchantAccount = merchantAccount
        request.amount = request.amount.copy(currency = currency)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("X-API-KEY", apiKey)
        val entity = HttpEntity(request, headers)
        val response = this.restTemplate.postForEntity("$endpoint/pal/servlet/Payment/v40/authorise", entity, Authorization::class.java)
        val result = response.body?.resultCode
        if ("Authorised" !== result) throw Unauthorized()
    }

    fun listRecurringDetails(shopperReference: String): RecurringDetail? {
        val request = RecurringDetailsRequest(shopperReference = shopperReference, merchantAccount = merchantAccount)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("X-API-KEY", apiKey)
        val entity = HttpEntity(request, headers)
        val response = this.restTemplate.postForEntity("$endpoint/pal/servlet/Recurring/v25/listRecurringDetails", entity, RecurringDetails::class.java).body
        return response?.details?.first()?.recurringDetail
    }

    fun pay(amount: BigDecimal, reference: String, shopperReference: String, recurringDetailReference: String) {
        val longValue = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString().replace(".", "").toLong()
        val request = PaymentRequest(
                amount = Amount(value = longValue, currency = currency),
                paymentMethod = PaymentMethod(recurringDetailReference),
                reference = reference,
                merchantAccount = merchantAccount,
                shopperReference = shopperReference)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("X-API-KEY", apiKey)
        val entity = HttpEntity(request, headers)
        val response = this.restTemplate.postForEntity("$endpoint/v41/payments", entity, Authorization::class.java)
        val result = response.body?.resultCode
        if ("Authorised" !== result) throw Unauthorized()
    }
}

class Unauthorized(message: String = "The credit card was not authorized") : RuntimeException()
