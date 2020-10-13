package br.com.netlit.library.domain.read.page;

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
@DynamoDBTable(tableName = "ReadPage")
public class ReadPage {

  @DynamoDBHashKey(attributeName = "Id")
  private String id;
  @DynamoDBRangeKey(attributeName = "ReadAt")
  @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
  private LocalDateTime time;
  @DynamoDBAttribute(attributeName = "PageId")
  private UUID pageId;

  public ReadPage(final String id) {
    this.id = id;
  }

  @Builder
  public ReadPage(final LocalDateTime time, final UUID accountId, final UUID bookId, final UUID pageId) {
    this.id = accountId + ":" + bookId;
    this.time = time;
    this.pageId = pageId;
  }

  public UUID getAccountId() {
    return UUID.fromString(this.id.split(":")[0]);
  }

  public UUID getBookId() {
    return UUID.fromString(this.id.split(":")[1]);
  }
}

