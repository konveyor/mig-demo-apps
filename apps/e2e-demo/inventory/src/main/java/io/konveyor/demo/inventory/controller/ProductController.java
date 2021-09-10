package io.konveyor.demo.inventory.controller;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import io.konveyor.demo.inventory.exception.ResourceNotFoundException;
import io.konveyor.demo.inventory.model.Product;
import io.konveyor.demo.inventory.service.IProductService;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;


@Path("/products")
@ApplicationScoped
public class ProductController {
	private static Logger logger = Logger.getLogger( ProductController.class.getName() );
	
	@Inject
	IProductService productService;
	
	@Inject
	Tracer tracer;
	
	@GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Product getById(@PathParam("id") Long id) {
		Product p;
		/* Use a try-with-resources block to ensure that the active span
		 * gets closed even in the case of exception.*/
		try (Scope scope = tracer
				.buildSpan("getById")
				.withTag("layer", "Controller")
				.startActive(true)){
			logger.debug("Entering ProductController.getById()");
			p = productService.findById(id);
			if (p == null) {
				throw new ResourceNotFoundException("Product not found");
			}
		}	
		return p;    
    }
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findAll(@QueryParam("sort") String sortString,
            @QueryParam("page") @DefaultValue("0") int pageIndex,
            @QueryParam("size") @DefaultValue("20") int pageSize) {
		Page page = Page.of(pageIndex, pageSize);
        Sort sort = getSortFromQuery(sortString);
        return Response.ok(productService.findAll(page, sort)).build();
	}

	/**
	 * This method tries to mimic the behavior of Spring MVC's @EnableSpringDataWebSupport annotation when it comes to the sort parameter.
	 * @param sortString The string containing the sort query to be used. Must have the "field,asc/desc" format or the second part of the query will be ignored.
	 * @return The {@link Sort} object with the sort criteria to apply.
	 */
	private Sort getSortFromQuery(String sortString) {
		if (sortString != null && !sortString.equals("")) {
			List<String> sortQuery = Arrays.asList(sortString.split(","));
			if (sortQuery == null || sortQuery.size()== 0 || sortQuery.size() >2) {	
				return null;
			}
			else {
				if (sortQuery.size() == 1) {
					return Sort.by(sortQuery.get(0));
				} else {
					if (sortQuery.get(1).equals("asc")) {
						return Sort.ascending(sortQuery.get(0));
					} else {
						if (sortQuery.get(1).equals("desc")) {
							return Sort.descending(sortQuery.get(0));
						} else {
							return Sort.by(sortQuery.get(0));
						}
					}
				}
			}	
		}
		return null;
	}

}
