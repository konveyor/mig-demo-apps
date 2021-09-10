package io.konveyor.demo.gateway.controller;

import io.konveyor.demo.gateway.exception.ResourceNotFoundException;
import io.konveyor.demo.gateway.model.Customer;
import io.konveyor.demo.gateway.service.CustomersService;
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
@RequestMapping("/customers")
@Component
@Slf4j
public class CustomersController {
	
	@Autowired
	private CustomersService customerService;
	
	@Autowired
	Tracer tracer;
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer getById(@PathVariable("id") Long id) {
		Customer c;
		Span span = tracer.buildSpan("getById").start();
		try{
			log.debug("Entering CustomerController.getById()");
			c = customerService.getById(id);
			if (c == null) {
				throw new ResourceNotFoundException("Requested customer doesn't exist");
			}
			log.debug("Returning element: " + c);
		} finally {
			span.finish();
		}
		return c;
	}
	
	@RequestMapping
	public Page<Customer> findAll(Pageable pageable){
		return customerService.findAll(pageable);
	}	
}
