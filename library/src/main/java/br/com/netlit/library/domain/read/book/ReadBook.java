package br.com.netlit.library.domain.read.book;

import br.com.netlit.library.infra.database.LocalDateTimeConverter;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "ReadBook")
public class ReadBook {

  @DynamoDBHashKey(attributeName = "Id")
  private String id;
  @DynamoDBIndexHashKey(attributeName = "AccountId", globalSecondaryIndexName = "AccountIndex")
  private UUID accountId;
  @DynamoDBIndexRangeKey(attributeName = "ReadAt", globalSecondaryIndexName = "AccountIndex")
  @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
  private LocalDateTime time;
  @DynamoDBAttribute(attributeName = "BookId")
  private UUID bookId;
  @DynamoDBAttribute(attributeName = "Percentage")
  private Double percentage;

  @Builder
  public ReadBook(final UUID accountId, final UUID bookId, final LocalDateTime time, final Double percentage) {
    this.id = accountId + ":" + bookId;
    this.accountId = accountId;
    this.bookId = bookId;
    this.time = time;
    this.percentage = percentage;
  }
}

