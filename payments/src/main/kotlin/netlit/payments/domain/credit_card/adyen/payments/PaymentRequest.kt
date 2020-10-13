package netlit.payments.domain.credit_card.adyen.payments

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("amount", "paymentMethod", "reference", "merchantAccount", "shopperReference", "shopperInteraction")
class PaymentRequest(
        @JsonProperty("amount")
        var amount: Amount? = Amount(value = 0, currency = "BRL"),
        @JsonProperty("paymentMethod")
        var paymentMethod: PaymentMethod? = null,
        @JsonProperty("reference")
        var reference: String? = null,
        @JsonProperty("merchantAccount")
        var merchantAccount: String? = null,
        @JsonProperty("shopperReference")
        var shopperReference: String? = null,
        @JsonProperty("shopperInteraction")
        var shopperInteraction: String? = "ContAuth")