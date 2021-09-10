package io.konveyor.demo.orders.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import io.konveyor.demo.orders.model.Order;
import io.konveyor.demo.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	Tracer tracer;
	
	/**
	 * Finds an {@link Order} using its {@code id} as search criteria
	 * @param id The {@link Order} {@code id}
	 * @return The {@link Order} with the supplied {@code id}, {@literal null} if no {@link Order} is found. 
	 */
	public Order findById(Long id) {
		Span span = tracer.buildSpan("findById").start();
		log.debug("Entering OrderService.findById()");
		Optional<Order> o = repository.findById(id);
		try {
			Order order = o.get();
			//Force lazy loading of the OrderItem list
			order.getItems().size();
			log.debug("Returning element: " + o);
			return order;
		} catch (NoSuchElementException nsee) {
			log.debug("No element found, returning null");
			return null;
		} finally {
			span.finish();
		}
	}
	
	public Page<Order>findAll(Pageable pageable) {
		log.debug("Entering OrderService.findAll()");
		Page<Order> p = repository.findAll(pageable);
		log.debug("Returning element: " + p);
		return p;
	}
}
