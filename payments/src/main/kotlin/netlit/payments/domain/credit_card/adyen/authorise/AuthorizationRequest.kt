package netlit.payments.domain.credit_card.adyen.authorise

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("additionalData", "amount", "reference", "merchantAccount", "shopperReference", "recurring")
data class AuthorizationRequest(
        @JsonProperty("additionalData")
        var additionalData: AdditionalDataRequest? = null,
        @JsonProperty("amount")
        var amount: Amount = Amount(value = 0, currency = "BRL"),
        @JsonProperty("reference")
        var reference: String? = null,
        @JsonProperty("merchantAccount")
        var merchantAccount: String = "",
        @JsonProperty("shopperReference")
        var shopperReference: String? = null,
        @JsonProperty("recurring")
        var recurring: Recurring = Recurring(contract = "RECURRING"))