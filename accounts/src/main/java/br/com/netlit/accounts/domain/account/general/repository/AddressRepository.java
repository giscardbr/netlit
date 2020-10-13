package br.com.netlit.accounts.domain.account.general.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import br.com.netlit.accounts.domain.account.general.entity.AddressEntity;
import br.com.netlit.accounts.domain.account.general.entity.AddressType;
import br.com.netlit.accounts.infra.database.DynamoDBRepository;

@Repository
public class AddressRepository extends DynamoDBRepository<AddressEntity, UUID> {

  public AddressRepository(final DynamoDBMapper mapper) {
    super(mapper, AddressEntity.class);
  }

  public Optional<AddressEntity> findBy(final UUID accountId, final AddressType type) {

   DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
   scanRequest.addFilterCondition("AccountId", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(accountId.toString())));
   scanRequest.addFilterCondition("Type", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(type.toString())));
	  
	  PaginatedScanList<AddressEntity> page = this.getMapper().scan(AddressEntity.class, scanRequest);

	  return !page.isEmpty() ? Optional.of(page.get(0)) : Optional.empty();
  }

}