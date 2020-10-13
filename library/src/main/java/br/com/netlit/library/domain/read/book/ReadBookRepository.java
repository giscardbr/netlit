package br.com.netlit.library.domain.read.book;

import br.com.netlit.library.infra.database.DynamoDBRepository;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class ReadBookRepository extends DynamoDBRepository<ReadBook, String> {

  public ReadBookRepository(final DynamoDBMapper mapper) {
    super(mapper, ReadBook.class);
  }

  public List<ReadBook> findAll() {
    return this.getMapper().scan(this.getType(), new DynamoDBScanExpression());
  }

  @Override
  public void save(final ReadBook entity) {
    this.getMapper().save(entity, DynamoDBMapperConfig.builder()
        .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
        .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.PUT)
        .build());
  }

  public Page<ReadBook> findBookIdsByAccountId(final UUID accountId, final Pageable pageable) {
    final DynamoDBQueryExpression<ReadBook> queryExpression =
        new DynamoDBQueryExpression<ReadBook>()
            .withIndexName("AccountIndex")
            .withProjectionExpression("BookId, Percentage")
            .withConsistentRead(false)
            .withScanIndexForward(false)
            .withHashKeyValues(ReadBook.builder()
                .accountId(accountId)
                .build());
    final PaginatedQueryList<ReadBook> query = this.getMapper().query(ReadBook.class, queryExpression);

    final int size = query.size();
    final int offset = Math.toIntExact(pageable.getOffset());
    final int limit = offset + pageable.getPageSize() > size ? size : offset + pageable.getPageSize();

    final List<ReadBook> books = IntStream.range(offset, limit)
        .mapToObj(query::get)
        .collect(Collectors.toList());
    return new PageImpl<>(books, pageable, size);
  }
}
