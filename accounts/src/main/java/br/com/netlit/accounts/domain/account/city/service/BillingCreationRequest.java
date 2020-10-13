package br.com.netlit.accounts.domain.account.city.service;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netlit.accounts.domain.account.mock.BillingCreationHttpRequest;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(toBuilder = true)
public class BillingCreationRequest {

	@JsonProperty("account_id")
	private UUID accountId;

	@JsonProperty("email")
	private String email;

	@JsonProperty("subscription_plan")
	private String subscriptionPlan;

	@JsonProperty("order_id")
	private Long orderId;

	@JsonProperty("credit_card")
	@NotEmpty(message = "{creditCard.notEmpty}")
	private String creditCard;

	public static BillingCreationRequest valueOf(final @NonNull BillingCreationHttpRequest httpRequest) {
		return builder()
				.subscriptionPlan(httpRequest.getSubscriptionPlan())
				.orderId(httpRequest.getOrderId())
				.creditCard(httpRequest.getCreditCard())
				.build();
	}
}
