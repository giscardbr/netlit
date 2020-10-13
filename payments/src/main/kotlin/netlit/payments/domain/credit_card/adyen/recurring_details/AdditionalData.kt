package netlit.payments.domain.credit_card.adyen.recurring_details

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("cardBin")
data class AdditionalData(
        @JsonProperty("cardBin")
        var cardBin: String? = null)