package br.com.netlit.accounts.domain.account.parent.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "year"
})
public class School {

	@JsonProperty("name")
	@NotEmpty(message = "{schoolName.notEmpty}")
	private String name;

	@JsonProperty("year")
	@NotEmpty(message = "{schoolYear.notEmpty}")
	private String year;
}
