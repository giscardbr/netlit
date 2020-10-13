package br.com.netlit.checkout.domain.checkout.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.netlit.checkout.domain.checkout.model.Order;
import br.com.netlit.checkout.domain.checkout.repository.OrderRepository;

@Service
@Transactional
public class OrderService {
 
	@Autowired
    private OrderRepository orderRepository;
 
    public OrderService(OrderRepository orderRepository) {
    	this.orderRepository = orderRepository;
    }
    
    public Iterable<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }
     
    public Order create(Order order) {
        order.setDateTimeCreated(LocalDateTime.now());
        return this.orderRepository.save(order);
    }
 
    public void update(Order order) {
        this.orderRepository.save(order);
    }

    public Optional<Order> getById(Long orderId) {
        return this.orderRepository.findById(orderId);
    }
 
}
