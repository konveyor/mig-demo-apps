package io.konveyor.demo.inventory.service;


import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import io.konveyor.demo.inventory.model.Product;
import io.konveyor.demo.inventory.repository.ProductRepository;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

@Transactional
@ApplicationScoped
public class ProductService implements IProductService {
	
	private static Logger logger = Logger.getLogger( ProductService.class.getName() );
	
	@Inject
	ProductRepository repository;
	
	@Inject
	Tracer tracer;
	
	/**
	 * Finds a {@link Product} using its {@code id} as search criteria
	 * @param id The {@link Product} {@code id}
	 * @return The {@link Product} with the supplied {@code id}, {@literal null} if no {@link Product} is found. 
	 */
	public Product findById(Long id) {
		Span childSpan = tracer.buildSpan("findById").start();
		childSpan.setTag("layer", "Service");
		logger.debug("Entering ProductService.findById()");
		Product p = repository.findById(id);
		childSpan.finish();
		return p;
	}

	@Override
	public List<Product> findAll(Page page, Sort sort) {
		Span childSpan = tracer.buildSpan("findAll").start();
		childSpan.setTag("layer", "Service");
		logger.debug("Entering ProductService.findAll()");
		List<Product> p = repository.findAll(page, sort);
		childSpan.finish();
		return p;
	}
	
	
}
