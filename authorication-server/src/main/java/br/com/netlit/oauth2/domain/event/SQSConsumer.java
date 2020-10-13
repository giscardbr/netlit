package br.com.netlit.oauth2.domain.event;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.netlit.oauth2.domain.entity.UserEntity;
import br.com.netlit.oauth2.domain.repository.UserRepository;
import br.com.netlit.oauth2.infra.messaging.Notification;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class SQSConsumer {

  private final ObjectMapper mapper;
  private final UserRepository userRepository;

  public SQSConsumer(final ObjectMapper mapper, final UserRepository userRepository) {
    this.mapper = mapper;
    this.userRepository = userRepository;
  }

  @SneakyThrows(IOException.class)
  public void receive(final Message message) {
    val messageBody = message.getBody();
    val notification = this.mapper.readValue(messageBody, Notification.class);
    val notificationMessage = notification.getMessage();
    log.info("Received message " + notification.getMessageId() + ": " + notificationMessage);
    final String topic = notification.getTopicArn();
    if (topic.contains("credentials-created-topic")) {
      val event = this.mapper.readValue(notificationMessage, CredentialsCreatedEvent.class);
      val user = new UserEntity();
      user.setAccountId(event.getAccountId());
      final String email = event.getEmail();
      user.setEmail(email);
      user.setPassword(event.getPassword());
      user.setRole(event.getRole());
      user.setEnabled(true);
      this.userRepository.save(user);
      log.info("The credentials of {} where created successfully!", email);
    } else if (topic.contains("credentials-updated-topic")) {
      val event = this.mapper.readValue(notificationMessage, CredentialsUpdatedEvent.class);
      val user = this.userRepository.findById(event.getUsername()).orElseThrow(NullUserException::new);
      user.setPassword(event.getPassword());
      this.userRepository.save(user);
      log.info("The credentials of {} where updated successfully!", user.getEmail());
    } else {
      log.info("No actions for notifications coming from {}", topic);
    }
  }
}
