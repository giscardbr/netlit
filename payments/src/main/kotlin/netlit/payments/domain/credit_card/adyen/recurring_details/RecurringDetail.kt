package netlit.payments.domain.credit_card.adyen.recurring_details

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("additionalData", "alias", "aliasType", "card", "contractTypes", "creationDate", "firstPspReference", "paymentMethodVariant", "recurringDetailReference", "variant")
data class RecurringDetail(
        @JsonProperty("additionalData")
        var additionalData: AdditionalData? = null,
        @JsonProperty("alias")
        var alias: String? = null,
        @JsonProperty("aliasType")
        var aliasType: String? = null,
        @JsonProperty("card")
        var card: Card? = null,
        @JsonProperty("contractTypes")
        var contractTypes: List<String> = ArrayList(),
        @JsonProperty("creationDate")
        var creationDate: String? = null,
        @JsonProperty("firstPspReference")
        var firstPspReference: String? = null,
        @JsonProperty("paymentMethodVariant")
        var paymentMethodVariant: String? = null,
        @JsonProperty("recurringDetailReference")
        var recurringDetailReference: String? = null,
        @JsonProperty("variant")
        var variant: String? = null)