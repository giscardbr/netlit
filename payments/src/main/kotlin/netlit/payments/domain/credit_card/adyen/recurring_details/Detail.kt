package netlit.payments.domain.credit_card.adyen.recurring_details

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("RecurringDetail")
data class Detail(
        @JsonProperty("RecurringDetail")
        var recurringDetail: RecurringDetail? = null)