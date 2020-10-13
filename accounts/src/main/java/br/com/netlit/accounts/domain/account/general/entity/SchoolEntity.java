package br.com.netlit.accounts.domain.account.general.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DynamoDBDocument
@NoArgsConstructor
public class SchoolEntity {

  @DynamoDBAttribute(attributeName = "Name")
  private String name;
  @DynamoDBAttribute(attributeName = "Year")
  private String year;
}
