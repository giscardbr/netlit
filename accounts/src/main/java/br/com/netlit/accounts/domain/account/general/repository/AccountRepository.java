package br.com.netlit.accounts.domain.account.general.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import br.com.netlit.accounts.domain.account.general.entity.AccountEntity;
import br.com.netlit.accounts.infra.database.DynamoDBRepository;

@Repository
public class AccountRepository extends DynamoDBRepository<AccountEntity, UUID> {

  public AccountRepository(final DynamoDBMapper mapper) {
    super(mapper, AccountEntity.class);
  }
  
    
    public boolean exists(final boolean isBusiness, final String value) {
  	  
  	   DynamoDBScanExpression scanRequest = new DynamoDBScanExpression();
  	   scanRequest.addFilterCondition((isBusiness ? "CNPJ" : "CPF"), new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(value)));
  	  
  	    return this.getMapper().count(this.getType(), scanRequest) > 0;
  	  }
  
}
