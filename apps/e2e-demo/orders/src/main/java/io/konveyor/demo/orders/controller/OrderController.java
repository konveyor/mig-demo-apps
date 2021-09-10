package io.konveyor.demo.orders.controller;

import java.util.List;

import io.konveyor.demo.orders.exception.ResourceNotFoundException;
import io.konveyor.demo.orders.model.Order;
import io.konveyor.demo.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	Tracer tracer;
	
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order getById(@PathVariable("id") Long id) {
		Order o;
		Span span = tracer.buildSpan("getById").start();
		try{
			log.debug("Entering OrderController.getById()");
			o = orderService.findById(id);
			if (o == null) {
				throw new ResourceNotFoundException("Requested order doesn't exist");
			}
			log.debug("Returning element: " + o);
		} finally {
			span.finish();
		}
		return o;
	}
	
	@RequestMapping
	public List<Order> findAll(Pageable pageable){
		return orderService.findAll(pageable).toList();
	}

}
