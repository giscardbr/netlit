package netlit.payments.domain.credit_card.adyen.payments

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("value", "currency")
data class Amount(
        @JsonProperty("value")
        var value: Long? = null,
        @JsonProperty("currency")
        var currency: String? = null)