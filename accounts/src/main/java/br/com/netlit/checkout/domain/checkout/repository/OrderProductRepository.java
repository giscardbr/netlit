package br.com.netlit.checkout.domain.checkout.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.netlit.checkout.domain.checkout.model.Order;

@Repository
public interface OrderProductRepository extends CrudRepository<Order, Long> {

}
