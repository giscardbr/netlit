package br.com.netlit.checkout.domain.checkout.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.netlit.checkout.domain.checkout.dto.ProductJson;
import br.com.netlit.checkout.domain.checkout.model.Product;
import br.com.netlit.checkout.domain.checkout.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
 
	@Autowired
    private ProductService productService;
 
    @GetMapping(value = { "", "/" })
    public @NotNull Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }

	@GetMapping(value = "/{productId}", produces = "application/hal+json")
	public ResponseEntity<ProductJson> findById(@PathVariable final Long productId) {
		
		Product product = this.productService.getProduct(productId);
		
		ProductJson productJson = ProductJson.builder()
			.id(product.getId())
			.name(product.getName())
			.price(product.getPrice())
			.description(product.getDescription())
			.protheusId(product.getProtheusId())
			.build();
		
		return new ResponseEntity<ProductJson>(productJson, HttpStatus.ACCEPTED);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<ProductJson> create(@RequestBody ProductJson productJson) {

		Product product = Product.builder()
				.id(productJson.getId())
				.name(productJson.getName())
				.price(productJson.getPrice())
				.description(productJson.getDescription())
				.protheusId(productJson.getProtheusId())
				.build();

		productService.save(product);
		
		productJson.setId(product.getId());
		
	    String uri = ServletUriComponentsBuilder
	      .fromCurrentServletMapping()
	      .path("/api/products/{id}")
	      .buildAndExpand(product.getId())
	      .toString();
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Location", uri);
	    
		return new ResponseEntity<>(productJson, headers, HttpStatus.CREATED);
	}
    
}
