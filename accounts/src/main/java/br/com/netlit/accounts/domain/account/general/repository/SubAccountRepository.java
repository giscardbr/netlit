package br.com.netlit.accounts.domain.account.general.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import br.com.netlit.accounts.infra.database.DynamoDBRepository;

@Repository
public class SubAccountRepository extends DynamoDBRepository<SubAccountEntity, UUID> {

	public SubAccountRepository(final DynamoDBMapper mapper) {
		super(mapper, SubAccountEntity.class);
	}

	public PaginatedScanList<SubAccountEntity> findBy(final UUID accountId) {

		DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
		scanRequest
			.addFilterCondition("AccountId", new Condition()
			.withComparisonOperator(ComparisonOperator.EQ)
			.withAttributeValueList(new AttributeValue(accountId.toString())));

		PaginatedScanList<SubAccountEntity> page = this.getMapper().scan(SubAccountEntity.class, scanRequest);

		return page;
	}
}
