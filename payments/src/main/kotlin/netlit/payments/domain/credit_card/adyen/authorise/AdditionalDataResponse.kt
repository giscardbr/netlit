package netlit.payments.domain.credit_card.adyen.authorise

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("recurringProcessingModel")
data class AdditionalDataResponse(
        @JsonProperty("recurringProcessingModel")
        var recurringProcessingModel: String? = null)