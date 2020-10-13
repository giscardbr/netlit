package br.com.netlit.checkout.domain.checkout.dto;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@JsonPropertyOrder({
    "items",
    "total_price"
	})
public class OrderCreateRequest {
 
	@JsonProperty("email")
	@NotNull(message = "{email-account.notEmpty}")
	private String email;

	@JsonProperty("items")
    private List<OrderProductJson> items = new ArrayList<>();
 
	@JsonProperty("total_price")
	private BigDecimal totalPrice;

   @Transient
    public BigDecimal getTotalOrderPrice() {
	   
		return getItems().stream().map(OrderProductJson::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

    }
 
    @Transient
    public int getNumberOfProducts() {
        return this.items.size();
    }
    
}
