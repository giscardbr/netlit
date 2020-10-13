package br.com.netlit.library.domain.bookmarks;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Bookmark")
public class BookmarkEntity {

  @DynamoDBHashKey(attributeName = "AccountId")
  private UUID accountId;
  @DynamoDBRangeKey(attributeName = "BookId")
  private UUID bookId;
}

