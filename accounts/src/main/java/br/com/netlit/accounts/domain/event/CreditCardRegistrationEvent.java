package br.com.netlit.accounts.domain.event;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.context.ApplicationEvent;

import br.com.netlit.accounts.domain.account.city.service.BillingCreationRequest;
import lombok.Value;

@Value
public class CreditCardRegistrationEvent extends ApplicationEvent {

	private final BillingCreationRequest billing;

	public CreditCardRegistrationEvent(
			final Object source, 
			@NotNull final UUID accountId, 
			@NotNull final String email,
			@NotNull @Valid final BillingCreationRequest billing 
			) {
		
		super(source);
		this.billing = billing.toBuilder()
				.accountId(accountId)
				.email(email)
				.build();
	}
}
