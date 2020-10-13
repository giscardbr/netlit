package br.com.netlit.checkout.domain.checkout.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.netlit.checkout.domain.checkout.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
