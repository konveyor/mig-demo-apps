package io.konveyor.demo.gateway.controller;

import io.konveyor.demo.gateway.exception.ResourceNotFoundException;
import io.konveyor.demo.gateway.model.Order;
import io.konveyor.demo.gateway.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Component
@Slf4j
public class OrdersController {
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	Tracer tracer;
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order getById(@PathVariable("id") Long id) {
		Order o;
		Span span = tracer.buildSpan("getById").start();
		try{
			log.debug("Entering OrderController.getById()");
			o = orderService.getById(id);
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
	public Page<Order> findAll(Pageable pageable){
		return orderService.findAll(pageable);
	}
}
