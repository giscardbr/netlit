package br.com.netlit.accounts.domain.account.general.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import br.com.netlit.accounts.domain.account.general.entity.SchoolRoomStudentEntity;
import br.com.netlit.accounts.infra.database.DynamoDBRepository;

@Repository
public class SchoolRoomStudentRepository extends DynamoDBRepository<SchoolRoomStudentEntity, UUID> {

	public SchoolRoomStudentRepository(final DynamoDBMapper mapper) {
		super(mapper, SchoolRoomStudentEntity.class);
	}

	public PaginatedScanList<SchoolRoomStudentEntity> findBy(final UUID schooGradeRoomId) {

		DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
		scanRequest
			.addFilterCondition("SchoolGradeRoomId", new Condition()
			.withComparisonOperator(ComparisonOperator.EQ)
			.withAttributeValueList(new AttributeValue(schooGradeRoomId.toString())));

		PaginatedScanList<SchoolRoomStudentEntity> page = this.getMapper().scan(SchoolRoomStudentEntity.class, scanRequest);

		return page;
	}

	public PaginatedScanList<SchoolRoomStudentEntity> findBySubAccountId(final UUID subAccountId) {

		DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
		scanRequest
			.addFilterCondition("SubAccountId", new Condition()
			.withComparisonOperator(ComparisonOperator.EQ)
			.withAttributeValueList(new AttributeValue(subAccountId.toString())));

		PaginatedScanList<SchoolRoomStudentEntity> page = this.getMapper().scan(SchoolRoomStudentEntity.class, scanRequest);

		return page;
	}
}
