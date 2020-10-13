package br.com.netlit.accounts.domain.account.parent.resource;

import br.com.netlit.accounts.domain.account.general.entity.AccountType;
import br.com.netlit.accounts.domain.account.general.entity.Gender;
import br.com.netlit.accounts.domain.account.mock.Credentials;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"id",
    "name",
    "last_name",
    "birth_date",
    "gender",
    "credentials",
    "school",
    "type",
    "created"
	})
public class ParentUserHttpRequest {

	@JsonProperty("id")
	private String id;

	@JsonProperty("birth_date")
	@NotNull(message = "{birthDate.notEmpty}")
	private @Past LocalDate birthDate;

	@JsonProperty("credentials")
	@NotNull(message = "{childrenCredentials.notEmpty}")
	private @Valid Credentials credentials;
	
	@JsonProperty("gender")
	@NotNull(message = "{gender.notEmpty}")
	private Gender gender;
	
	@JsonProperty("last_name")
	@NotEmpty(message = "{lastName.notEmpty}")
	private String lastName;
	
	@JsonProperty("name")
	@NotEmpty(message = "{name.notEmpty}")
	private String name;
	
	@JsonProperty("school")
	@NotNull(message = "{childrenSchool.notEmpty}")
	private @Valid School school;
	
	@JsonProperty("type")
	private @Valid AccountType type;
	
	@JsonProperty("created")
	private LocalDateTime created;
}
