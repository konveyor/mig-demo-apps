package io.konveyor.demo.gateway.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.konveyor.demo.gateway.model.Customer;
import io.konveyor.demo.util.PaginatedResponse;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerRepository extends GenericRepository{
	
	@Autowired
	Tracer tracer;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${services.customers.url}")
	String customersServiceURL;
	
	@HystrixCommand(commandKey = "Customers", fallbackMethod = "getFallbackCustomer", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
	})
	public Customer getCustomerById(Long id) {
		Span span = tracer.buildSpan("getCustomerById").start();
		log.debug("Entering OrdersService.getCustomerById()");
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(customersServiceURL)
				.pathSegment( "{customer}");		
		Customer c = restTemplate.getForObject(
				builder.buildAndExpand(id).toUriString(), 
				Customer.class);
		//Trigger fallback if no result is obtained.
		if (c == null) {
			throw new RuntimeException();
		}
		log.debug(c.toString());
		span.finish();
		return c;
	}
	
	@HystrixCommand(commandKey = "AllCustomers", fallbackMethod = "getFallbackCustomers", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
	})
	public List<Customer> findAll(Pageable pageable) {
		Span span = tracer.buildSpan("findAll").start();
		log.debug("Entering CustomerRepository.findAll()");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(customersServiceURL)
				.queryParam("page", pageable.getPageNumber())
				.queryParam("size", pageable.getPageSize())
				.queryParam("sort", getSortString(pageable));
		ResponseEntity<PaginatedResponse<Customer>> responseEntity = 
				  restTemplate.exchange(
						  builder.toUriString(),
						  HttpMethod.GET,
						  null,
						  new ParameterizedTypeReference<PaginatedResponse<Customer>>() {}
				  );
		List<Customer> customers = responseEntity.getBody().getContent();
		span.finish();
		return customers;
	}
	
	public Customer getFallbackCustomer(Long id, Throwable e) {
		log.warn("Failed to obtain Customer, " + e.getMessage() + " for customer with id " + id);
		Customer c = new Customer();
		c.setId(id);
		c.setUsername("Unknown");
		c.setName("Unknown");
		c.setSurname("Unknown");
		c.setAddress("Unknown");
		c.setCity("Unknown");
		c.setCountry("Unknown");
		c.setZipCode("Unknown");
		return c;
	}
	
	public List<Customer> getFallbackCustomers(Pageable pageable, Throwable e) {
		log.warn("Failed to obtain Customers, " + e.getMessage());
		return new ArrayList<Customer>();
	}

}
