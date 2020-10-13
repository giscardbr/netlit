package br.com.netlit.accounts.domain.account.general.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import br.com.netlit.accounts.domain.account.general.entity.SchoolGradeRoomEntity;
import br.com.netlit.accounts.infra.database.DynamoDBRepository;

@Repository
public class SchoolGradeRoomRepository extends DynamoDBRepository<SchoolGradeRoomEntity, UUID> {

	public SchoolGradeRoomRepository(final DynamoDBMapper mapper) {
		super(mapper, SchoolGradeRoomEntity.class);
	}

	public PaginatedScanList<SchoolGradeRoomEntity> findBy(final UUID accountId) {

		DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
		scanRequest
			.addFilterCondition("SubAccountId", new Condition()
			.withComparisonOperator(ComparisonOperator.EQ)
			.withAttributeValueList(new AttributeValue(accountId.toString())));

		PaginatedScanList<SchoolGradeRoomEntity> page = this.getMapper().scan(SchoolGradeRoomEntity.class, scanRequest);

		return page;
	}

	public PaginatedScanList<SchoolGradeRoomEntity> findBy(final String code) {

		DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
		scanRequest
			.addFilterCondition("Code", new Condition()
			.withComparisonOperator(ComparisonOperator.EQ)
			.withAttributeValueList(new AttributeValue(code)));

		PaginatedScanList<SchoolGradeRoomEntity> page = this.getMapper().scan(SchoolGradeRoomEntity.class, scanRequest);

		return page;
	}
}
