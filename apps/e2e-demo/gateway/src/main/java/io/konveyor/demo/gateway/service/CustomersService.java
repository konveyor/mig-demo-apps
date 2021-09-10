package io.konveyor.demo.gateway.service;

import java.util.List;

import io.konveyor.demo.gateway.model.Customer;
import io.konveyor.demo.gateway.repository.CustomerRepository;
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
public class CustomersService {
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public Page<Customer> findAll(Pageable pageable) {
		Span span = tracer.buildSpan("findAll").start();
		log.debug("Entering OrdersService.findAll()");
		List<Customer> orders = customerRepository.findAll(pageable);
		span.finish();
		return new PageImpl<Customer>(orders, pageable, orders.size());
	}

	public Customer getById(Long id) {
		Span span = tracer.buildSpan("getById").start();
		log.debug("Entering CustomersService.getById()");
		Customer c = customerRepository.getCustomerById(id);
		span.finish();
		return c;
	}

}
