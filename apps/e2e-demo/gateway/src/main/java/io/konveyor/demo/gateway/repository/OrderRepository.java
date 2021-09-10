package io.konveyor.demo.gateway.repository;

import java.util.ArrayList;
import java.util.List;

import io.konveyor.demo.gateway.model.Order;
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

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderRepository extends GenericRepository {
	@Autowired
	Tracer tracer;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${services.orders.url}")
	String ordersServiceURL;
	
	@HystrixCommand(commandKey = "Orders", fallbackMethod = "getFallbackOrder", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
	})
	public Order getOrderById(Long id) {
		Span span = tracer.buildSpan("getOrderById").start();
		log.debug("Entering OrderRepository.getOrderById()");
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(ordersServiceURL)
				.pathSegment( "{order}");
		Order o = restTemplate.getForObject(
				builder.buildAndExpand(id).toUriString(), 
				Order.class);
		if (o == null)
			log.debug("Obtained null order");
		else
			log.debug(o.toString());
		span.finish();
		return o;
	}
	
	@HystrixCommand(commandKey = "AllOrders", fallbackMethod = "getFallbackOrders", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
	})
	public List<Order> findAll(Pageable pageable) {
		Span span = tracer.buildSpan("findAll").start();
		log.debug("Entering OrderRepository.findAll()");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ordersServiceURL)
				.queryParam("page", pageable.getPageNumber())
				.queryParam("size", pageable.getPageSize())
				.queryParam("sort", getSortString(pageable));
		ResponseEntity<List<Order>> responseEntity = 
				  restTemplate.exchange(
						  builder.toUriString(),
						  HttpMethod.GET,
						  null,
						  new ParameterizedTypeReference<List<Order>>() {}
				  );
		List<Order> orders = responseEntity.getBody();
		span.finish();
		return orders;
	}
	

	public Order getFallbackOrder(Long id, Throwable e) {
		log.warn("Failed to obtain Order, " + e.getMessage() + " for order with id " + id);
		return null;
	}
	
	public List<Order> getFallbackOrders(Pageable pageable, Throwable e) {
		log.warn("Failed to obtain Orders, " + e.getMessage());
		return new ArrayList<Order>();
	}
	
}
