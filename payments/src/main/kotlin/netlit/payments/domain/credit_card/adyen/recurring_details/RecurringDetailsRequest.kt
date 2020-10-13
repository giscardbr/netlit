package netlit.payments.domain.credit_card.adyen.recurring_details

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("recurring", "shopperReference", "merchantAccount")
data class RecurringDetailsRequest(
        @JsonProperty("recurring")
        var recurring: Recurring = Recurring(),
        @JsonProperty("shopperReference")
        var shopperReference: String? = null,
        @JsonProperty("merchantAccount")
        var merchantAccount: String? = null)