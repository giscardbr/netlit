package br.com.netlit.checkout.domain.checkout.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderProductPK implements Serializable {
 
    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;
 
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order.getId() == null) ? 0 : order.getId().hashCode());
		result = prime * result + ((product.getId() == null) ? 0 : product.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderProductPK other = (OrderProductPK) obj;
		if (order.getId() == null) {
			if (other.order.getId() != null)
				return false;
		} else if (!order.getId().equals(other.order.getId()))
			return false;
		if (product.getId() == null) {
			if (other.product.getId() != null)
				return false;
		} else if (!product.getId().equals(other.product.getId()))
			return false;
		return true;
	}
 
}