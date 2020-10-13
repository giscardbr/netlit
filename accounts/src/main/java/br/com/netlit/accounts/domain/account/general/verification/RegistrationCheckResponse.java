package br.com.netlit.accounts.domain.account.general.verification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationCheckResponse {
	
	@JsonProperty("account_type")
	private AccountType accountType;

}
