package br.com.netlit.oauth2.domain.entity;

import java.time.LocalDate;
import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import br.com.netlit.oauth2.infra.database.LocalDateConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

	@DynamoDBTypeConverted(converter = LocalDateConverter.class)
	@DynamoDBAttribute(attributeName = "Created")
	private LocalDate created;

	public UserEntity(final String email) {
		super();
		this.email = email;
	}

	public UserEntity(String email, String password, UUID accountId, Role role, boolean enabled, LocalDate created) {
		super();
		this.email = email;
		this.password = password;
		this.accountId = accountId;
		this.role = role;
		this.enabled = enabled;
		this.created = created;
	}

}
