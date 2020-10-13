package br.com.netlit.library.domain.bookmarks;

import br.com.netlit.library.infra.database.DynamoDBRepository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.amazonaws.services.dynamodbv2.model.ComparisonOperator.EQ;

@Repository
public class BookmarkEntityRepository extends DynamoDBRepository<BookmarkEntity, UUID> {

  public BookmarkEntityRepository(final DynamoDBMapper mapper) {
    super(mapper, BookmarkEntity.class);
  }

  public Page<BookmarkEntity> findByAccountId(final UUID accountId, final Pageable pageable) {
    final DynamoDBQueryExpression<BookmarkEntity> queryExpression =
        new DynamoDBQueryExpression<BookmarkEntity>()
            .withProjectionExpression("BookId")
            .withConsistentRead(false)
            .withHashKeyValues(BookmarkEntity.builder()
                .accountId(accountId)
                .build());
    final PaginatedQueryList<BookmarkEntity> query = this.getMapper().query(BookmarkEntity.class, queryExpression);

    final int size = query.size();
    final int offset = Math.toIntExact(pageable.getOffset());
    final int limit = offset + pageable.getPageSize() > size ? size : offset + pageable.getPageSize();

    final List<BookmarkEntity> books = IntStream.range(offset, limit)
        .mapToObj(query::get)
        .collect(Collectors.toList());
    return new PageImpl<>(books, pageable, size);
  }

  public Optional<BookmarkEntity> findByIds(final UUID accountId, final UUID bookId) {
    final DynamoDBQueryExpression<BookmarkEntity> queryExpression =
        new DynamoDBQueryExpression<BookmarkEntity>()
            .withConsistentRead(false)
            .withLimit(1)
            .withHashKeyValues(BookmarkEntity.builder()
                .accountId(accountId)
                .build())
            .withRangeKeyCondition("BookId", new Condition()
                .withComparisonOperator(EQ)
                .withAttributeValueList(new AttributeValue().withS(bookId.toString())));
    final PaginatedQueryList<BookmarkEntity> page = this.getMapper().query(BookmarkEntity.class, queryExpression);
    return !page.isEmpty() ? Optional.of(page.get(0)) : Optional.empty();
  }
}
