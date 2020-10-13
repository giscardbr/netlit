package br.com.netlit.accounts.domain.account.general.entity;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Address")
public class AddressEntity {

  @DynamoDBAutoGeneratedKey
  @DynamoDBHashKey(attributeName = "Id")
  private UUID id;
  @DynamoDBIndexHashKey(attributeName = "AccountId", globalSecondaryIndexName = "AccountIndex")
  private UUID accountId;
  @DynamoDBAttribute(attributeName = "AdditionalInformation")
  private String additionalInformation;
  @DynamoDBAttribute(attributeName = "City")
  private @NotEmpty String city;
  @DynamoDBAttribute(attributeName = "District")
  private @NotEmpty String district;
  @DynamoDBAttribute(attributeName = "Number")
  private @NotNull Long number;
  @DynamoDBAttribute(attributeName = "Phone")
  private @NotEmpty String phone;
  @DynamoDBAttribute(attributeName = "State")
  private @NotEmpty String state;
  @DynamoDBAttribute(attributeName = "Street")
  private @NotEmpty String street;
  @DynamoDBAttribute(attributeName = "ZIP")
  private @NotEmpty String zip;
  @DynamoDBAttribute(attributeName = "CityCode")
  private @NotEmpty String cityCode;

  @DynamoDBTypeConvertedEnum
  @DynamoDBAttribute(attributeName = "Type")
  private AddressType type;

}
