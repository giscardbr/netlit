package br.com.netlit.accounts.domain.account.general.resource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "gradeRooms"
	})
public class SchoolGradeRoomResponse {
	
	private List<SchoolGradeRoomRequest> gradeRooms;
	
}
