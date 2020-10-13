package br.com.netlit.library.infra.database;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class DynamoDBMapperProvider {

  private final String prefix;

  public DynamoDBMapperProvider(@Value("${aws.dynamodb.table-prefix}") final String prefix) {
    this.prefix = prefix;
  }

  @Bean
  public DynamoDBMapper dynamoDbMapper(final AmazonDynamoDB dynamoDB) {
    return new DynamoDBMapper(dynamoDB, DynamoDBMapperConfig.TableNameOverride
        .withTableNamePrefix(this.prefix)
        .config());
  }
}
