package br.com.netlit.accounts.domain.account.general.entity;

import java.time.LocalDate;
import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import br.com.netlit.accounts.infra.database.LocalDateConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@DynamoDBTable(tableName = "Account")
public class AccountEntity {

	@DynamoDBAutoGeneratedKey
	@DynamoDBHashKey(attributeName = "Id")
	private UUID id;
	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "Type")
	private AccountType type;
	@DynamoDBAttribute(attributeName = "Name")
	private String name;
	@DynamoDBAttribute(attributeName = "BirthDate")
	@DynamoDBTypeConverted(converter = LocalDateConverter.class)
	private LocalDate birthDate;
	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "Gender")
	private Gender gender;
	@DynamoDBAttribute(attributeName = "LastName")
	private String lastName;
	@DynamoDBAttribute(attributeName = "Mobile")
	private String mobile;
	@DynamoDBAttribute(attributeName = "RG")
	private String rg;
	@DynamoDBAttribute(attributeName = "CPF")
	private String cpf;
	@DynamoDBAttribute(attributeName = "CNPJ")
	private String cnpj;
	@DynamoDBAttribute(attributeName = "Readers")
	private Long readers;

	@DynamoDBTypeConverted(converter = LocalDateConverter.class)
	@DynamoDBAttribute(attributeName = "Created")
	private LocalDate created;

	@DynamoDBAttribute(attributeName = "EntityName")
	private String entityName;
	@DynamoDBAttribute(attributeName = "EntityNumber")
	private String entityNumber;
	@DynamoDBAttribute(attributeName = "TradingName")
	private String tradingName;
	@DynamoDBAttribute(attributeName = "IsBusiness")
	private boolean isBusiness;
	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "Status")
	private AccountStatus status;
	
	public AccountEntity(String cpf) {
		super();
		this.cpf = cpf;
	}

}
