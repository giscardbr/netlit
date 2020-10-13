package br.com.netlit.checkout.domain.checkout.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@JsonPropertyOrder({
	"id",
    "name",
    "price",
    "description",
    "protheus_id"
	})
public class ProductJson {

	@JsonProperty("id")
    private Long id;

	@JsonProperty("name")
    private String name;

	@JsonProperty("price")
    private BigDecimal price;
 
	@JsonProperty("description")
    private String description;

	@JsonProperty("protheus_id")
	private String protheusId;
}
