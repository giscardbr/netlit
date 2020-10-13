package br.com.netlit.accounts.domain.event;

import org.springframework.context.ApplicationEvent;

import lombok.NonNull;
import lombok.Value;

@Value
public class EmailEvent extends ApplicationEvent {

	private final String from;
	private final String [] to;
	private final String subject;
	private final String body;
	private final boolean isHtml;

	public EmailEvent(
			@NonNull final Object source, 
			@NonNull final String body, 
					 final String from,
			@NonNull final String [] to, 
			@NonNull final String subject,
			final boolean isHtml) {
		super(source);
		this.body = body;
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.isHtml = isHtml;
	}
}
