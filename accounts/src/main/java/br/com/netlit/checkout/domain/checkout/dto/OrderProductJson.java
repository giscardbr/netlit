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
	"product_id", 
	"quantity", 
	"total_price" 
	})
public class OrderProductJson {

	@JsonProperty("product_id")
	private Long productId;

	@JsonProperty("quantity")
	private Integer quantity;

	@JsonProperty("total_price")
	private BigDecimal totalPrice;

}
