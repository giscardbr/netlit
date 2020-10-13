package br.com.netlit.accounts.domain.account.mock;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "email", "password" })
public class Credentials {

	@JsonProperty("email")
	@NotEmpty(message = "{email.notEmpty}")
	private String email;

	@NotEmpty(message = "{password.notEmpty}")
	@JsonProperty("password")
	private String password;
}
