package br.com.netlit.library.domain.review;

import java.time.LocalDateTime;
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
	"email",
	"book_id",
	"rating",
	"commnent",
	"created"
})public class ReviewCreateResponse {


	@JsonProperty("email")
	private String email;

	@JsonProperty("bookId")
	private UUID bookId;

	@JsonProperty("rating")
	private int ratng;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("created")
	private LocalDateTime created;

}
