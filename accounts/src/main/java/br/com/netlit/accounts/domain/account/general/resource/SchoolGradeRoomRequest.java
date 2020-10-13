package br.com.netlit.accounts.domain.account.general.resource;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.netlit.accounts.domain.account.general.entity.SchoolGradeRoomEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "code",
    "teacher"
	})
public class SchoolGradeRoomRequest {

	@JsonProperty("id")
	private UUID id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("code")
	private String code;

	@JsonProperty("teacher")
	private UUID subAccountId;
	
	public static SchoolGradeRoomRequest valueOf(SchoolGradeRoomEntity entity) {
		
		return SchoolGradeRoomRequest
			.builder()
			.id(entity.getId())
			.name(entity.getName())
			.code(entity.getCode())
			.subAccountId(entity.getSubAccountId())
			.build();
		
	}
}
