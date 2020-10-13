package br.com.netlit.checkout.domain.checkout.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import br.com.netlit.checkout.domain.checkout.model.Product;
import br.com.netlit.checkout.domain.checkout.repository.ProductRepository;

@Service
@Transactional
public class ProductService {
 
    private ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
    	this.productRepository = productRepository;
    }
 
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }
 
    public Product getProduct(long id) {
        return productRepository
          .findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
 
    public Product save(Product product) {
        return productRepository.save(product);
    }
}