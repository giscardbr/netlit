package br.com.netlit.accounts.infra.messaging;

import br.com.netlit.accounts.domain.event.CredentialsUpdatedEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class NewCredentialsSNSNotifier {

  private final ObjectMapper mapper;
  private final AmazonSNS sns;
  private final String topic;

  public NewCredentialsSNSNotifier(final ObjectMapper mapper,
                                   final AmazonSNS sns,
                                   @Value("${aws.sns.credentials.updated.topic}") final String topic) {
    log.info("Using topic " + topic);
    this.mapper = mapper;
    this.sns = sns;
    this.topic = topic;
  }

  @EventListener
  @SneakyThrows(JsonProcessingException.class)
  public void publish(final CredentialsUpdatedEvent event) {
    final String message = this.mapper.writeValueAsString(event);
    this.publish(message);
  }

  @Async
  protected void publish(final String message) {
    log.info("Publishing message");
    final PublishRequest request = new PublishRequest(this.topic, message);
    final PublishResult response = this.sns.publish(request);
    log.info("Message " + response.getMessageId() + " published");
  }
}
