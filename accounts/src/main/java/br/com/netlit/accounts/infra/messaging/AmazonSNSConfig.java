package br.com.netlit.accounts.infra.messaging;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Log4j2
@Configuration
public class AmazonSNSConfig {

  @Bean
  @Profile("dev")
  public AmazonSNS amazonSNSDev(@Value("${aws.region}") final String region) {
    return AmazonSNSAsyncClientBuilder.standard()
        .withRegion(region)
        .build();
  }

  @Bean
  @Profile("prod")
  public AmazonSNS amazonSNS() {
    return AmazonSNSAsyncClientBuilder.standard()
        .build();
  }
}
