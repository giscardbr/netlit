package netlit.payments.domain.credit_card.adyen.recurring_details

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("expiryMonth", "expiryYear", "holderName", "number")
data class Card(
        @JsonProperty("expiryMonth")
        var expiryMonth: String? = null,
        @JsonProperty("expiryYear")
        var expiryYear: String? = null,
        @JsonProperty("holderName")
        var holderName: String? = null,
        @JsonProperty("number")
        var number: String? = null)