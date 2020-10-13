package br.com.netlit.library.domain.review;

import java.time.LocalDateTime;
import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import br.com.netlit.library.infra.database.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Review")
public class ReviewEntity {

	@DynamoDBHashKey(attributeName = "Email")
	private String email;

	@DynamoDBRangeKey(attributeName = "BookId")
	private UUID bookId;

	@DynamoDBAttribute(attributeName = "Rating")
	private int rating;

	@DynamoDBAttribute(attributeName = "Comment")
	private String comment;

	@DynamoDBAttribute(attributeName = "Created")
	@DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
	private LocalDateTime created;

}
