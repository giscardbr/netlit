package br.com.netlit.accounts.domain.account.general.verification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"email", "password", "account_type"
})
public class RegistrationCreateRequest {

	@JsonProperty("email")
	private String email;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("account_type")
	private AccountType accountType;
}
