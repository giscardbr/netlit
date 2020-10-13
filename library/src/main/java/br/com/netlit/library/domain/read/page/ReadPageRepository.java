package br.com.netlit.library.domain.read.page;

import br.com.netlit.library.infra.database.DynamoDBRepository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import lombok.val;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ReadPageRepository extends DynamoDBRepository<ReadPage, String> {

  public ReadPageRepository(final DynamoDBMapper mapper) {
    super(mapper, ReadPage.class);
  }

  public Optional<ReadPage> findLatestByAccountAndBook(final UUID accountId, final UUID bookId) {
    final val mapper = this.getMapper();
    final val expression = new DynamoDBQueryExpression<ReadPage>()
        .withHashKeyValues(new ReadPage(accountId + ":" + bookId))
        .withProjectionExpression("ReadAt, PageId")
        .withScanIndexForward(false)
        .withConsistentRead(false)
        .withLimit(1);
    final PaginatedQueryList<ReadPage> page = mapper.query(this.getType(), expression);
    return !page.isEmpty() ? Optional.of(page.get(0)) : Optional.empty();
  }

  public Set<UUID> findReadPageIdsByAccountAndBook(final UUID accountId, final UUID bookId) {
    final val mapper = this.getMapper();
    final val expression = new DynamoDBQueryExpression<ReadPage>()
        .withHashKeyValues(new ReadPage(accountId + ":" + bookId))
        .withProjectionExpression("PageId")
        .withConsistentRead(false);
    final PaginatedQueryList<ReadPage> page = mapper.query(this.getType(), expression);
    return page.stream().map(ReadPage::getPageId).collect(Collectors.toSet());
  }
}
