package br.com.netlit.library.domain.review;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"book_id",
	"rating",
	"commnent"
})public class ReviewCreateRequest {


	@JsonProperty("book_id")
	private UUID bookId;

	@JsonProperty("rating")
	private int rating;

	@JsonProperty("comment")
	private String comment;

}
