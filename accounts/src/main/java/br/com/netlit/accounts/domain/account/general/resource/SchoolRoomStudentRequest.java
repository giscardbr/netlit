package br.com.netlit.accounts.domain.account.general.resource;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "code",
    "student"
	})
public class SchoolRoomStudentRequest {

	@JsonProperty("code")
	private String code;
	
	@JsonProperty("student")
	private UUID subAccountIdStudent;
	
}
