package br.com.netlit.checkout.domain.upload.service;

import org.springframework.context.ApplicationEvent;

import lombok.Value;

@Value
public class UploadProcessRequestEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private final Long entityId;
	private final boolean sendMail;
	private final boolean generatePassword;

	public UploadProcessRequestEvent(
			final Object source, 
			final Long entityId,
			final boolean sendMail,
			final boolean generatePassword
			) {
		super(source);
		this.entityId = entityId;
		this.sendMail = sendMail;
		this.generatePassword = generatePassword;
	}
	
}
