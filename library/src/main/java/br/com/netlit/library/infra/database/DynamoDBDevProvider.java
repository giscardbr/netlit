package br.com.netlit.library.infra.database;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Log4j2
@Configuration
@Profile("dev")
public class DynamoDBDevProvider {

  @Bean
  public AmazonDynamoDB amazonDynamoDB(
      @Value("${aws.region}") final String region,
      @Value("${aws.access-key-id}") final String accessKey,
      @Value("${aws.secret-access-key}") final String secretKey) {

    if (region == null || accessKey == null || secretKey == null) return AmazonDynamoDBClientBuilder.standard().build();
    return AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
        .withRegion(region)
        .build();
  }
}
