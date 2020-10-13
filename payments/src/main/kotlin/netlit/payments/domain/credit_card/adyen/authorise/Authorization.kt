package netlit.payments.domain.credit_card.adyen.authorise

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(NON_NULL)
@JsonPropertyOrder("additionalData", "pspReference", "resultCode", "authCode")
data class Authorization(
        @JsonProperty("additionalData")
        var additionalData: AdditionalDataResponse? = null,
        @JsonProperty("pspReference")
        var pspReference: String? = null,
        @JsonProperty("resultCode")
        var resultCode: String? = null,
        @JsonProperty("authCode")
        var authCode: String? = null)