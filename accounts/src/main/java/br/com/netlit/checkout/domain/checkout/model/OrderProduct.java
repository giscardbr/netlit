package br.com.netlit.checkout.domain.checkout.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_PRODUCT")
public class OrderProduct {
 
    @EmbeddedId
    @JsonIgnore
    private OrderProductPK pk;
 
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;
    
    public OrderProduct(Order order, Product product, Integer quantity) {
        pk = new OrderProductPK();
        pk.setOrder(order);
        pk.setProduct(product);
        this.quantity = quantity;
    }
 
    @Transient
    public Product getProduct() {
        return this.pk.getProduct();
    }
    
    @Override
    public boolean equals(Object obj) {
    	OrderProduct otherObj = (OrderProduct) obj;
    	return this.getProduct().getId().equals(otherObj.getProduct().getId());
    }
 
}
