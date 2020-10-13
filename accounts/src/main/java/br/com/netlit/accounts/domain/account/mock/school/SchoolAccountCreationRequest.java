package br.com.netlit.accounts.domain.account.mock.school;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.netlit.accounts.domain.account.city.service.BillingCreationRequest;
import br.com.netlit.accounts.domain.account.general.address.AddressCreationRequest;
import br.com.netlit.accounts.domain.account.general.entity.Gender;
import br.com.netlit.accounts.infra.utils.validation.CpfCnpj;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "last_name",
    "birth_date",
    "gender",
    "billing",
    "entity_number",
    "entity_name",
    "trading_name",
    "cnpj",
    "address"
	})
public class SchoolAccountCreationRequest {

	@JsonProperty("email")
//	@NotEmpty(message = "{email-account.notEmpty}")
	private String email;

	@JsonProperty("last_name")
	private String lastName;

	@JsonProperty("name")
	private String name;

	@JsonProperty("birth_date")
	@NotNull(message = "{birthDate.notEmpty}")
	private @Past @NotNull LocalDate birthDate;
	
	@JsonProperty("gender")
	private @NotNull Gender gender;

	@JsonProperty("cnpj")
	@NotEmpty(message = "{cnpj.notEmpty}")
	@CpfCnpj(message = "{cnpj.invalid}")
	private String cnpj;

	@JsonProperty("entity_number")
	private String entityNumber;

	@JsonProperty("entity_name")
	private String entityName;
	
	@JsonProperty("trading_name")
	private String tradingName;

	@NotNull(message = "{address.notEmpty}")
	private final @Valid AddressCreationRequest address;

	@NotNull(message = "{billing.notEmpty}")
	private final @Valid BillingCreationRequest billing;
	
}
