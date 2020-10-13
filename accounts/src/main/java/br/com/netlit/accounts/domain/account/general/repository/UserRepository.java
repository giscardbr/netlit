package br.com.netlit.accounts.domain.account.general.repository;

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

import br.com.netlit.accounts.domain.account.general.entity.UserEntity;
import br.com.netlit.accounts.infra.database.DynamoDBRepository;

@Repository
public class UserRepository extends DynamoDBRepository<UserEntity, String> {

  public UserRepository(final DynamoDBMapper mapper) {
    super(mapper, UserEntity.class);
  }

  public boolean exists(final String email) {
	    return Optional.ofNullable(email)
	        .map(UserEntity::new)
	        .map(user -> new DynamoDBQueryExpression<UserEntity>().withHashKeyValues(user))
	        .map(queryExpression -> this.getMapper().count(this.getType(), queryExpression))
	        .map(total -> total > 0)
	        .orElse(Boolean.FALSE);
	  }
  
  public PaginatedScanList<UserEntity> findBy(final UUID accountId) {

	   DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
	   scanRequest.addFilterCondition("AccountId", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(accountId.toString())));
		  
		  PaginatedScanList<UserEntity> page = this.getMapper().scan(UserEntity.class, scanRequest);

		  return page;
	  }
  
}
