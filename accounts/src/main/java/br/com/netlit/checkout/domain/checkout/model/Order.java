package br.com.netlit.checkout.domain.checkout.model;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS")
public class Order {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL")
    private String email;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateTimeCreated;
 
    @Column(name = "STATUS")
    private OrderStatus status;
    
    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;
 
    @Column(name = "PROTHEUS_STATUS")
    private String protheusStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "pk.order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Valid
    private List<OrderProduct> orderProducts = new ArrayList<>();
 
    @Transient
    public BigDecimal calculateOrderAmount() {

    	return getOrderProducts().stream().map(OrderProduct::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

    }
 
    @Transient
    public int getNumberOfProducts() {
        return this.orderProducts.size();
    }
 
}
