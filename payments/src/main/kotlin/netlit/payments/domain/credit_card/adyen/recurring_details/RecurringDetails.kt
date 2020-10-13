package netlit.payments.domain.credit_card.adyen.recurring_details

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*

@JsonInclude(NON_NULL)
@JsonPropertyOrder("creationDate", "details", "lastKnownShopperEmail", "shopperReference")
data class RecurringDetails(
        @JsonProperty("creationDate")
        var creationDate: String? = null,
        @JsonProperty("details")
        var details: List<Detail> = ArrayList(),
        @JsonProperty("lastKnownShopperEmail")
        var lastKnownShopperEmail: String? = null,
        @JsonProperty("shopperReference")
        var shopperReference: String? = null)