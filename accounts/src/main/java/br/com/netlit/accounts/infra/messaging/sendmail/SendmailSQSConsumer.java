package br.com.netlit.accounts.infra.messaging.sendmail;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.accounts.domain.account.general.verification.MailClient;
import br.com.netlit.accounts.domain.event.EmailEvent;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class SendmailSQSConsumer {

	@Autowired
	private MailClient mailClient;

	private final ObjectMapper mapper;

	public SendmailSQSConsumer(final ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@SneakyThrows(IOException.class)
	public void receive(final Message message) {
		
		log.info("Received message " + message.toString());
		
		val messageBody = message.getBody();
		val notification = this.mapper.readValue(messageBody, Notification.class);
		
		val email = this.mapper.readValue(notification.getMessage(), EmailEvent.class);
		
		mailClient.sendEmail(email.getSubject(), email.getTo(), email.getBody(), email.isHtml());
	}
}
