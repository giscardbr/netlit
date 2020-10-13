package br.com.netlit.library.infra.database;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Log
@Configuration
public class DynamoDBEmbeddedConfig {

  @Bean
  @Profile("test")
  public AmazonDynamoDB amazonDynamoDB() {
    log.info("Using embedded DynamoDB for tests purposes");
    final AmazonDynamoDBLocal dbLocal = DynamoDBEmbedded.create();
    return dbLocal.amazonDynamoDB();
  }

  @Bean
  public DynamoDB dynamoDB(final AmazonDynamoDB client) {
    return new DynamoDB(client);
  }

  @PostConstruct
  public void setupDynamoDBEmbeddedLibraries() {
    System.setProperty("sqlite4java.library.path", "target/dynamodb-libs");
  }
}
