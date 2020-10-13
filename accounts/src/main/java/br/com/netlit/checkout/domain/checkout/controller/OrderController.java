package br.com.netlit.checkout.domain.checkout.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import br.com.netlit.accounts.domain.account.error.OrderCreateException;
import br.com.netlit.checkout.domain.checkout.dto.OrderCreateRequest;
import br.com.netlit.checkout.domain.checkout.dto.OrderCreateResponse;
import br.com.netlit.checkout.domain.checkout.dto.OrderProductJson;
import br.com.netlit.checkout.domain.checkout.model.Order;
import br.com.netlit.checkout.domain.checkout.model.OrderProduct;
import br.com.netlit.checkout.domain.checkout.model.OrderProductPK;
import br.com.netlit.checkout.domain.checkout.model.OrderStatus;
import br.com.netlit.checkout.domain.checkout.model.ProductType;
import br.com.netlit.checkout.domain.checkout.service.OrderService;
import br.com.netlit.checkout.domain.checkout.service.ProductService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private static final String FREE_PRODUCT_VALIDATION_MESSAGE = "free.product.validation.message";

	private static final Long QTY_OFFER_PROFESSOR_BONIFICADO = 20L;
 
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
    private OrderService orderService;
 
	@Autowired
    private ProductService productService;
	
	@GetMapping(value = "/{orderId}", produces = "application/hal+json")
	public ResponseEntity<OrderCreateResponse> findById(@PathVariable final Long orderId) {

		Order order = orderService.getById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		
		return new ResponseEntity<OrderCreateResponse>(OrderCreateResponse.valueOf(order), HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@Valid @RequestBody OrderCreateRequest req) {

		String freeProductValidationMessage = messageSource.getMessage(FREE_PRODUCT_VALIDATION_MESSAGE, null, LocaleContextHolder.getLocale());
		
		Object[] array = req.getItems().stream().map(i -> i.getProductId()).toArray();
		
		long qtyDistinct = Arrays.asList(array).stream().distinct().count();
		
		if (qtyDistinct != req.getItems().size()) {
			throw new ValidationException("Item do pedido duplicado");
		}
		
		Order order = Order.builder()
				.dateTimeCreated(LocalDateTime.now())
				.totalPrice(req.getTotalPrice())
				.status(OrderStatus.CART)
				.email(req.getEmail())
				.build();

		order.setTotalPrice(req.getTotalPrice());
		

		List<OrderProduct> listItems = new ArrayList<OrderProduct>();
		
		for (OrderProductJson orderProductJson : req.getItems()) {
			
			OrderProduct op = new OrderProduct();
			
			op.setPk(OrderProductPK.builder()
					.order(order)
					.product(productService.getProduct(orderProductJson.getProductId()))
					.build()
					);
			
			op.setQuantity(orderProductJson.getQuantity());
			op.setTotalPrice(orderProductJson.getTotalPrice());
			
			listItems.add(op);
		}
		order.setOrderProducts(listItems);

		listItems.stream().forEach(i -> {
			BigDecimal calculatedTotal = BigDecimal.valueOf(i.getQuantity()).multiply(i.getProduct().getPrice()).setScale(2, RoundingMode.CEILING);
			if (!i.getTotalPrice().setScale(2, RoundingMode.CEILING).equals(calculatedTotal)) {
				throw new OrderCreateException("O valor informado deve ser igual ao valor calculado. Item: ["+ i.toString() +"]");
			}
		});

		if (!order.getTotalPrice().setScale(2, RoundingMode.CEILING).equals(order.calculateOrderAmount().setScale(2, RoundingMode.CEILING))) {
			throw new OrderCreateException("O valor informado deve ser igual ao valor calculado. Order: ["+ order.toString() +"]");
		}
		
		OrderProduct itemProfessorBonificado = listItems.stream().filter(i -> i.getProduct().getType().equals(ProductType.PROFESSOR_BONIFICADO)).findAny().orElse(null);
		Integer qtyItemProfessorBonificado = (null != itemProfessorBonificado ? itemProfessorBonificado.getQuantity() : 0);

		OrderProduct itemAluno = listItems.stream().filter(i -> i.getProduct().getType().equals(ProductType.ALUNO)).findAny().orElse(null);
		Integer qtyItemAluno = (null != itemAluno ? itemAluno.getQuantity() : 0);

		long qtyCalcProfessorBonificado = qtyItemAluno / QTY_OFFER_PROFESSOR_BONIFICADO;
		
		if (qtyCalcProfessorBonificado != qtyItemProfessorBonificado) {
			throw new ValidationException(freeProductValidationMessage);
		}
		
		this.orderService.create(order);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
	    org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
	    headers.add("Location", uri.toString());
	    
		return new ResponseEntity<>(OrderCreateResponse.valueOf(order), headers, HttpStatus.CREATED);
	}

}
