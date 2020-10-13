package br.com.netlit.accounts.domain.account.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"subscription_plan", 
	"address", 
	"credit_card",
	"order_id"
})
public class BillingCreationHttpRequest {

	@JsonProperty("subscription_plan")
	@NotEmpty(message = "subscriptionPlan.notEmpty")
	private String subscriptionPlan;

	@JsonProperty("address")
	private @NotNull @Valid Address address;

	@JsonProperty("credit_card")
	private @NotEmpty String creditCard;

	@JsonProperty("order_id")
	private Long orderId;

}
