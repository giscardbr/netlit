package br.com.netlit.checkout.domain.upload.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

import br.com.netlit.checkout.domain.upload.model.UploadEntity;
import lombok.Value;

@Value
public class UploadRequestEvent extends ApplicationEvent {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private final String accountId;
	private final MultipartFile file;
	private UploadEntity entity;
	private final boolean sendMail;
	private final boolean generatePassword;

	public UploadRequestEvent(
			final Object source,
			final UploadEntity entity,
			final String accountId,
			final MultipartFile file,
			final boolean sendMail,
			final boolean generatePassword
			) {
		super(source);
		this.accountId = accountId;
		this.file = file;
		this.entity = entity;
		this.sendMail = sendMail;
		this.generatePassword = generatePassword;
	}

}
