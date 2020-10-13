package br.com.netlit.accounts.domain.account.general.repository;

import br.com.netlit.accounts.domain.account.general.entity.CredentialsEntity;
import br.com.netlit.accounts.infra.database.DynamoDBRepository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CredentialsRepository extends DynamoDBRepository<CredentialsEntity, String> {

  public CredentialsRepository(final DynamoDBMapper mapper) {
    super(mapper, CredentialsEntity.class);
  }

  public Optional<CredentialsEntity> findByAccountId(final UUID accountId) {
    final DynamoDBQueryExpression<CredentialsEntity> queryExpression =
        new DynamoDBQueryExpression<CredentialsEntity>()
            .withIndexName("AccountIndex")
            .withProjectionExpression("Email")
            .withConsistentRead(false)
            .withScanIndexForward(false)
            .withLimit(1)
            .withHashKeyValues(CredentialsEntity.builder()
                .accountId(accountId)
                .build());
    final PaginatedQueryList<CredentialsEntity> page = this.getMapper().query(CredentialsEntity.class, queryExpression);
    return !page.isEmpty() ? Optional.of(page.get(0)) : Optional.empty();
  }

  public boolean exists(final String email) {
    return Optional.ofNullable(email)
        .map(CredentialsEntity::new)
        .map(credentials -> new DynamoDBQueryExpression<CredentialsEntity>().withHashKeyValues(credentials))
        .map(queryExpression -> this.getMapper().count(this.getType(), queryExpression))
        .map(total -> total > 0)
        .orElse(Boolean.FALSE);
  }
}