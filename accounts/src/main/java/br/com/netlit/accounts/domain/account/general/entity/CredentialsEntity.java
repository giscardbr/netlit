package br.com.netlit.accounts.domain.account.general.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Credentials")
public class CredentialsEntity {

  @DynamoDBHashKey(attributeName = "Email")
  private String email;
  @DynamoDBIndexHashKey(attributeName = "AccountId", globalSecondaryIndexName = "AccountIndex")
  private UUID accountId;
  @DynamoDBTypeConvertedEnum
  @DynamoDBAttribute(attributeName = "AccountType")
  private AccountType accountType;
  @DynamoDBTypeConvertedEnum
  @DynamoDBAttribute(attributeName = "AccountRole")
  private AccountRole accountRole;

  public CredentialsEntity(final String email) {
    this.email = email;
  }
}
