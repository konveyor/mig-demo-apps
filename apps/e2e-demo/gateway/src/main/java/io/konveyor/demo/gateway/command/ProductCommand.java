package io.konveyor.demo.gateway.command;

import io.konveyor.demo.gateway.model.OrderItem;
import io.konveyor.demo.gateway.model.Product;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductCommand extends HystrixCommand<OrderItem>{
	private OrderItem item;
	
	String inventoryServiceURL;
	
	RestTemplate restTemplate;
	
	public ProductCommand(OrderItem item, String inventoryServiceURL, RestTemplate restTemplate){
		super( HystrixCommandGroupKey.Factory.asKey( "Products" ), HystrixThreadPoolKey.Factory.asKey( "ProductsThreads" ) );
		this.item = item;
		this.inventoryServiceURL = inventoryServiceURL;
		this.restTemplate = restTemplate;
	}

	@Override
	protected OrderItem run() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(inventoryServiceURL)
				.pathSegment( "{product}");
		Product p = restTemplate.getForObject(
				builder.buildAndExpand(item.getProduct().getId()).toUriString(), 
				Product.class);
		if (p != null) {
			item.setProduct(p);
		}
		return item;
	}
	
	@Override
	protected OrderItem getFallback() {
		log.warn( "Failed to obtain product, " + getFailedExecutionException().getMessage() + " for order line " + item );
		return item;
	}
	
}
