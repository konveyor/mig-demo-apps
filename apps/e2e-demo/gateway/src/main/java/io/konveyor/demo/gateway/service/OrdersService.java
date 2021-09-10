package io.konveyor.demo.gateway.service;

import java.util.List;

import io.konveyor.demo.gateway.model.Order;
import io.konveyor.demo.gateway.repository.CustomerRepository;
import io.konveyor.demo.gateway.repository.InventoryRepository;
import io.konveyor.demo.gateway.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdersService {
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	public Order getById(Long id) {		
		Span span = tracer.buildSpan("getById").start();
		log.debug("Entering OrdersService.getById()");
		Order o = orderRepository.getOrderById(id);
		if (o != null) {
			o.setCustomer(customerRepository.getCustomerById(o.getCustomer().getId()));
			o.setItems(inventoryRepository.getProductDetails(o.getItems()));
		}
		span.finish();
		return o;
	}

	public Page<Order> findAll(Pageable pageable) {
		Span span = tracer.buildSpan("findAll").start();
		log.debug("Entering OrdersService.findAll()");
		List<Order> orders = orderRepository.findAll(pageable);
		for (Order o : orders) {
			o.setCustomer(customerRepository.getCustomerById(o.getCustomer().getId()));
			o.setItems(inventoryRepository.getProductDetails(o.getItems()));
		}
		span.finish();
		return new PageImpl<Order>(orders, pageable, orders.size());
	}	

}
