package br.com.netlit.accounts.infra.messaging.sendmail;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Log4j2
@Configuration
public class AmazonSQSConfig {

  @Bean
  @Profile({"dev", "test"})
  public AmazonSQS amazonSQSDev(@Value("${aws.region}") final String region) {
    return AmazonSQSAsyncClientBuilder.standard()
        .withRegion(region)
        .build();
  }

  @Bean
  @Profile("prod")
  public AmazonSQS amazonSQS() {
    return AmazonSQSAsyncClientBuilder.standard()
        .build();
  }
}
