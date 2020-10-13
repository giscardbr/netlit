package br.com.netlit.accounts.domain.account.general.resource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.netlit.accounts.domain.account.general.entity.SubAccountEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "student"
	})
public class SchoolRoomStudentResponse {

	private List<SubAccountEntity> students;
	
}
