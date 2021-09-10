package io.konveyor.demo.inventory.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import io.konveyor.demo.inventory.model.Product;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

	private static Logger logger = Logger.getLogger( ProductRepository.class.getName() );
	
	@Inject
	Tracer tracer;
	
	public Product findById(Long id) {
		Span childSpan = tracer.buildSpan("findById").start();
		childSpan.setTag("layer", "Repository");
		logger.debug("Entering ProductRepository.findById()");
		Product p = find("id", id).firstResult();
		childSpan.finish();
		return p;
	}
	
	public List<Product> findAll(Page page, Sort sort) {
		Span childSpan = tracer.buildSpan("findAll").start();
		childSpan.setTag("layer", "Repository");
		logger.debug("Entering ProductRepository.findAll()");
		List<Product> p = Product.findAll(sort)
				.page(page)
				.list();
		childSpan.finish();
		return p;
	}

}
