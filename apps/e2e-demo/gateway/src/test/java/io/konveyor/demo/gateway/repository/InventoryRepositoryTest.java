package io.konveyor.demo.gateway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.konveyor.demo.gateway.model.OrderItem;
import io.konveyor.demo.gateway.model.Product;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;

@RunWith(SpringRunner.class)
public class InventoryRepositoryTest {
	
	@TestConfiguration
    static class CustomerRepositoryTestContextConfiguration {
		@Bean
        public Tracer tracer() {
        	return new Configuration("gateway").getTracer();
        }
		
		@Bean
		public static PropertySourcesPlaceholderConfigurer properties() {
			final PropertySourcesPlaceholderConfigurer pspc = 
					new PropertySourcesPlaceholderConfigurer();
			final Properties properties = new Properties();
			properties.setProperty("services.inventory.url", "http://inventory.svc:8080/products");
			properties.setProperty("hystrix.threadpool.ProductsThreads.coreSize", "20");
			pspc.setProperties(properties);
			return pspc;
		}
		
		@Bean
		public InventoryRepository repository() {
			return new InventoryRepository();
		}
	}
	
	@MockBean
	RestTemplate restTemplate;
	
	@Autowired
	InventoryRepository repository;
	
	private List<OrderItem> items;
	
	private Product product1;
	
	private Product product2;
	
	@Before
	public void setup() {
		product1 = new Product();
		product1.setId(new Long(1));
		product1.setName("Test Product 1");
		product1.setDescription("Test Description 1");
		
		product2 = new Product();
		product2.setId(new Long(2));
		product2.setName("Test Product 2");
		product2.setDescription("Test Description 2");
		
		OrderItem item1 = new OrderItem();
		item1.setPrice(new BigDecimal(30));
		item1.setQuantity(3);
		item1.setProduct((Product)SerializationUtils.clone(product1));
		
		OrderItem item2 = new OrderItem();
		item2.setPrice(new BigDecimal(50));
		item2.setQuantity(2);
		item2.setProduct((Product)SerializationUtils.clone(product2));
		
		items = new ArrayList<OrderItem>();
		
		items.add(item1);
		items.add(item2);
	}
	
	@Test
	public void getProductDetailsExistingTest() {
		
		Product p1 = new Product();
		p1.setId(new Long(1));
		
		Product p2 = new Product();
		p2.setId(new Long(2));
		
		OrderItem i1 = new OrderItem();
		i1.setPrice(new BigDecimal(30));
		i1.setQuantity(3);
		i1.setProduct(p1);
		
		OrderItem i2 = new OrderItem();
		i2.setPrice(new BigDecimal(50));
		i2.setQuantity(2);
		i2.setProduct(p2);
		
		List<OrderItem> incomplete = new ArrayList<OrderItem>();
		
		incomplete.add(i1);
		incomplete.add(i2);
		
		Mockito.when(restTemplate.getForObject("http://inventory.svc:8080/products/1", Product.class))
		.thenReturn(product1);
		
		Mockito.when(restTemplate.getForObject("http://inventory.svc:8080/products/2", Product.class))
		.thenReturn(product2);
		
		List<OrderItem> found = repository.getProductDetails(incomplete);
		
		assertThat(found).isEqualTo(items);
	}
	
	@Test
	public void getProductDetailsOneNonExistingTest() {
		
		Product p1 = new Product();
		p1.setId(new Long(1));
		
		Product p2 = new Product();
		p2.setId(new Long(2));
		
		OrderItem i1 = new OrderItem();
		i1.setPrice(new BigDecimal(30));
		i1.setQuantity(3);
		i1.setProduct(p1);
		
		OrderItem i2 = new OrderItem();
		i2.setPrice(new BigDecimal(50));
		i2.setQuantity(2);
		i2.setProduct(p2);
		
		List<OrderItem> incomplete = new ArrayList<OrderItem>();
		
		incomplete.add(i1);
		incomplete.add(i2);
		
		Mockito.when(restTemplate.getForObject("http://inventory.svc:8080/products/1", Product.class))
		.thenReturn(null);
		
		Mockito.when(restTemplate.getForObject("http://inventory.svc:8080/products/2", Product.class))
		.thenReturn(product2);
		
		List<OrderItem> found = repository.getProductDetails(incomplete);
		
		items.get(0).setProduct(p1);
		
		assertThat(found).isEqualTo(items);
	}
	
	@Test
	public void getProductDetailsRestExceptionTest() {
		
		Product p1 = new Product();
		p1.setId(new Long(1));
		
		Product p2 = new Product();
		p2.setId(new Long(2));
		
		OrderItem i1 = new OrderItem();
		i1.setPrice(new BigDecimal(30));
		i1.setQuantity(3);
		i1.setProduct(p1);
		
		OrderItem i2 = new OrderItem();
		i2.setPrice(new BigDecimal(50));
		i2.setQuantity(2);
		i2.setProduct(p2);
		
		List<OrderItem> incomplete = new ArrayList<OrderItem>();
		
		incomplete.add(i1);
		incomplete.add(i2);
		
		Mockito.when(restTemplate.getForObject("http://inventory.svc:8080/products/1", Product.class))
		.thenThrow(new RestClientException("Failed 1"));
		
		Mockito.when(restTemplate.getForObject("http://inventory.svc:8080/products/2", Product.class))
		.thenThrow(new RestClientException("Failed 2"));
		
		List<OrderItem> found = repository.getProductDetails(incomplete);
		
		assertThat(found).isEqualTo(incomplete);
	}
	
	
}
