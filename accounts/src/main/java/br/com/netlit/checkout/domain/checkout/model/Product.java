package br.com.netlit.checkout.domain.checkout.model;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@Table(name = "PRODUCT")
public class Product {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
 
    @Column(name = "PROTHEUS_ID")
    private String protheusId; 
    
    @NotNull(message = "Product name is required.")
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
 
    @Column(name = "PRICE")
    private BigDecimal price;
 
    @Column(name = "DESCRIPTION")
    private String description;
 
    @Column(name = "TYPE")
    private ProductType type;

}