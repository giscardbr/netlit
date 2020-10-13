package br.com.netlit.accounts.domain.account.general.verification;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import br.com.netlit.accounts.infra.database.DynamoDBRepository;

@Repository
public class VerificationTokenRepository extends DynamoDBRepository<VerificationTokenEntity, UUID> {

  public VerificationTokenRepository(final DynamoDBMapper mapper) {
    super(mapper, VerificationTokenEntity.class);
  }

  public Optional<VerificationTokenEntity> findByToken(final String token) {

   DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
   scanRequest.addFilterCondition("Token", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(token)));
	  
	  PaginatedScanList<VerificationTokenEntity> page = this.getMapper().scan(VerificationTokenEntity.class, scanRequest);

	  return !page.isEmpty() ? Optional.of(page.get(0)) : Optional.empty();
  }

  public Optional<VerificationTokenEntity> findByEmail(final String email) {

	   DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
	   scanRequest.addFilterCondition("Email", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(email)));
		  
		  PaginatedScanList<VerificationTokenEntity> page = this.getMapper().scan(VerificationTokenEntity.class, scanRequest);

		  return !page.isEmpty() ? Optional.of(page.get(0)) : Optional.empty();
  }
  
  public boolean exists(final String token) {
	    return Optional.ofNullable(token)
	        .map(VerificationTokenEntity::new)
	        .map(user -> new DynamoDBQueryExpression<VerificationTokenEntity>().withHashKeyValues(user))
	        .map(queryExpression -> this.getMapper().count(this.getType(), queryExpression))
	        .map(total -> total > 0)
	        .orElse(Boolean.FALSE);
	  }

}