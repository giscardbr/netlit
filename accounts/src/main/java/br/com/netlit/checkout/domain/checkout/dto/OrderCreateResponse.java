package br.com.netlit.checkout.domain.checkout.dto;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.netlit.checkout.domain.checkout.model.Order;
import br.com.netlit.checkout.domain.checkout.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@JsonPropertyOrder({
	"id",
    "created",
    "status",
    "items",
    "total_price"
	})
public class OrderCreateResponse {

	@JsonProperty("id")
    private Long id;
 
	@JsonProperty("email")
	private String email;

	@JsonProperty("created")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime created;
 
	@JsonProperty("status")
    private OrderStatus status;
 
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

    public static OrderCreateResponse valueOf(final Order order) {

    	OrderCreateResponse orderJson = OrderCreateResponse.builder()
				.id(order.getId())
				.created(order.getDateTimeCreated())
				.status(order.getStatus())
				.totalPrice(order.getTotalPrice())
				.email(order.getEmail())
				.build();
		
		orderJson.setItems(
				order.getOrderProducts().stream().map(
						op -> OrderProductJson.builder()
							.productId(op.getProduct().getId())
							.quantity(op.getQuantity())
							.totalPrice(op.getTotalPrice())
							.build()
						).collect(Collectors.toList())
				);
		return orderJson;
    	
    }
}
