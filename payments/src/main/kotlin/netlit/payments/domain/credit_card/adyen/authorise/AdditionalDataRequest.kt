package netlit.payments.domain.credit_card.adyen.authorise

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("card.encrypted.json")
data class AdditionalDataRequest(
        @JsonProperty("card.encrypted.json")
        var cardEncryptedJson: String? = null)
