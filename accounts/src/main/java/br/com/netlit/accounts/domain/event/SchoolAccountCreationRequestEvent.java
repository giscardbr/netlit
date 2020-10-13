package br.com.netlit.accounts.domain.event;

import org.springframework.context.ApplicationEvent;

import br.com.netlit.accounts.domain.account.mock.school.SchoolAccountCreationRequest;
import lombok.Value;

@Value
public class SchoolAccountCreationRequestEvent extends ApplicationEvent {

	private final SchoolAccountCreationRequest request;

	public SchoolAccountCreationRequestEvent(final Object source, final SchoolAccountCreationRequest request) {
		super(source);
		this.request = request;
	}
}
