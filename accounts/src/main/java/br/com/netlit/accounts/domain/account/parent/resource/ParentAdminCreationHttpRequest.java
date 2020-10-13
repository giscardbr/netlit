package br.com.netlit.accounts.domain.account.parent.resource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.netlit.accounts.domain.account.general.entity.Gender;
import br.com.netlit.accounts.domain.account.mock.BillingCreationHttpRequest;
import br.com.netlit.accounts.infra.utils.validation.CpfCnpj;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "last_name",
    "birth_date",
    "gender",
    "mobile",
    "cpf",
    "rg",
    "readers",
    "accounts",
    "billing",
    "entity_number",
    "entity_name",
    "trading_name",
    "cnpj",
    "is_business",
    "email"
	})
public class ParentAdminCreationHttpRequest {

	@JsonProperty("accounts")
	private List<@Valid @NotNull ParentUserHttpRequest> accounts = new ArrayList<>();

	@JsonProperty("billing")
	private @Valid @NotNull BillingCreationHttpRequest billing;

	@JsonProperty("birth_date")
	@NotNull(message = "{birthDate.notEmpty}")
	private @Past LocalDate birthDate;

	@JsonProperty("cpf")
	@NotNull(message = "{cpf.notEmpty}")
	@CpfCnpj(message = "{cpf.invalid}")
	private String cpf;
	
	@JsonProperty("gender")
	@NotNull(message = "{gender.notEmpty}")
	private Gender gender;
	
	@JsonProperty("last_name")
	@NotNull(message = "{lastName.notEmpty}")
	private String lastName;
	
	@JsonProperty("mobile")
	@NotEmpty(message = "{mobile.notEmpty}")
	private String mobile;
	
	@JsonProperty("name")
	@NotNull(message = "{name.notEmpty}")
	private String name;
	
	@JsonProperty("rg")
	private @NotEmpty String rg;
	
	@JsonProperty("readers")
	private @NotNull Long readers;
	
	@JsonProperty("entity_number")
	private String entityNumber;
	
	@JsonProperty("entity_name")
	private String entityName;
	
	@JsonProperty("trading_name")
	private String tradingName;
	
	@JsonProperty("cnpj")
	@CpfCnpj(message = "{cnpj.invalid}")
	private String cnpj;
	
	@JsonProperty("is_business")
	private boolean isBusiness;
	
	@JsonProperty("email")
	@NotEmpty(message = "{email-account.notEmpty}")
	private String email;
	
}
