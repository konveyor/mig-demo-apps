package io.konveyor.demo.gateway.service;

import java.util.List;

import io.konveyor.demo.gateway.model.Product;
import io.konveyor.demo.gateway.repository.InventoryRepository;
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
public class InventoryService {
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	public Page<Product> findAll(Pageable pageable) {
		Span span = tracer.buildSpan("findAll").start();
		log.debug("Entering OrdersService.findAll()");
		List<Product> orders = inventoryRepository.findAll(pageable);
		span.finish();
		return new PageImpl<Product>(orders, pageable, orders.size());
	}

	public Product getById(Long id) {
		Span span = tracer.buildSpan("getById").start();
		log.debug("Entering CustomersService.getById()");
		Product p = inventoryRepository.getProductById(id);
		span.finish();
		return p;
	}

}
