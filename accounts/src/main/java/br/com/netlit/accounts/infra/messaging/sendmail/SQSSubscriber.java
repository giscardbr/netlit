package br.com.netlit.accounts.infra.messaging.sendmail;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class SQSSubscriber {
	
  private static final int PERIOD_IN_SECONDS = 2;
  private final ScheduledExecutorService executor = Executors
      .newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
          .setNameFormat("sqs-polling")
          .setDaemon(true)
          .setUncaughtExceptionHandler((t, e) -> log.error("Thread " + t.getName() + " execution failed", e))
          .build());

  private final AmazonSQS sqs;
  private final SendmailSQSConsumer consumer;
  private final String queue;
  private final ReceiveMessageRequest request;

  public SQSSubscriber(final AmazonSQS sqs,
                       final SendmailSQSConsumer consumer,
                       @Value("${aws.sqs.queue.sendmail}") final String queue) {

  	String format = "# %-90s#";
  	String border = String.format(format, "#").replace(" ", "#");
	System.out.println(border);
	System.out.println(border);
	  
    log.info("Subscribing to " + queue);
    this.sqs = sqs;
    this.consumer = consumer;
    this.queue = queue;
    this.request = new ReceiveMessageRequest()
        .withQueueUrl(queue)
        .withWaitTimeSeconds(20);
  }

  @PostConstruct
  public void init() {

	  	String format = "# %-90s#";
	  	String border = String.format(format, "#").replace(" ", "#");
		System.out.println(border);
		System.out.println(border);

		log.info("Starting polling queue");
    this.executor.scheduleAtFixedRate(this::waitForMessages, PERIOD_IN_SECONDS, PERIOD_IN_SECONDS, TimeUnit.SECONDS);
  }

  @PreDestroy
  public void finish() {
    log.info("Stopping polling queue");
    this.executor.shutdown();
  }

  private void waitForMessages() {
//    log.debug("Waiting for messages");
    final val messages = this.sqs.receiveMessage(this.request).getMessages();
//    log.debug("Got " + messages.size());
    messages.forEach(this::consume);
  }

  private void consume(final Message message) {
	  	String format = "# %-90s#";
	  	String border = String.format(format, "#").replace(" ", "#");
		System.out.println(border);
		System.out.println(border);
    final val messageId = message.getMessageId();
    try {
      this.consumer.receive(message);
      this.sqs.deleteMessage(this.queue, message.getReceiptHandle());
      log.info("Message " + messageId + " consumed successfully");
    } catch (Exception e) {
      log.error("Error when consuming message " + messageId, e);
    }
  }
}

