package io.konveyor.demo.gateway.controller;

import io.konveyor.demo.gateway.exception.ResourceNotFoundException;
import io.konveyor.demo.gateway.model.Product;
import io.konveyor.demo.gateway.service.InventoryService;
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
@RequestMapping("/products")
@Component
@Slf4j
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	Tracer tracer;
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getById(@PathVariable("id") Long id) {
		Product p;
		Span span = tracer.buildSpan("getById").start();
		try{
			log.debug("Entering InventoryController.getById()");
			p = inventoryService.getById(id);
			if (p == null) {
				throw new ResourceNotFoundException("Requested product doesn't exist");
			}
			log.debug("Returning element: " + p);
		} finally {
			span.finish();
		}
		return p;
	}
	
	@RequestMapping
	public Page<Product> findAll(Pageable pageable){
		return inventoryService.findAll(pageable);
	}

}
