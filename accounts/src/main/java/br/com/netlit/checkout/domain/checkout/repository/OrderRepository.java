package br.com.netlit.checkout.domain.checkout.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.netlit.checkout.domain.checkout.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

	@Query("select e from Order e where e.email = ?1 and e.id = ?2")
	Order findByEmailAndOrderId(String email, Long orderId);

}
