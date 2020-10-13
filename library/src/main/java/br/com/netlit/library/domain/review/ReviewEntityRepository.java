package br.com.netlit.library.domain.review;

import static com.amazonaws.services.dynamodbv2.model.ComparisonOperator.EQ;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;

import br.com.netlit.library.infra.database.DynamoDBRepository;

@Repository
public class ReviewEntityRepository extends DynamoDBRepository<ReviewEntity, UUID> {

  public ReviewEntityRepository(final DynamoDBMapper mapper) {
    super(mapper, ReviewEntity.class);
  }

  public Optional<ReviewEntity> findByIds(final String email, final UUID bookId) {
    final DynamoDBQueryExpression<ReviewEntity> queryExpression =
        new DynamoDBQueryExpression<ReviewEntity>()
            .withConsistentRead(false)
            .withLimit(1)
            .withHashKeyValues(ReviewEntity.builder()
                .email(email)
                .build())
            .withRangeKeyCondition("BookId", new Condition()
                .withComparisonOperator(EQ)
                .withAttributeValueList(new AttributeValue().withS(bookId.toString())));
    final PaginatedQueryList<ReviewEntity> page = this.getMapper().query(ReviewEntity.class, queryExpression);
    return !page.isEmpty() ? Optional.of(page.get(0)) : Optional.empty();
  }
}
