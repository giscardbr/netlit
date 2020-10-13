package br.com.netlit.accounts.domain.account.general.entity;

import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@DynamoDBTable(tableName = "User")
public class UserEntity {

	@DynamoDBHashKey(attributeName = "Email")
	private String email;
	
	@DynamoDBAttribute(attributeName = "Password")
	private String password;
	
	@DynamoDBAttribute(attributeName = "AccountId")
	private UUID accountId;
	
	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "Role")
	private Role role;

	@DynamoDBAttribute(attributeName = "Enabled")
	private boolean enabled;

	public UserEntity(String email, String password, UUID accountId, Role role, boolean enabled) {
		super();
		this.email = email;
		this.password = password;
		this.accountId = accountId;
		this.role = role;
		this.enabled = enabled;
	}

	public UserEntity(String email) {
		this.email = email;
	}
}
