package io.konveyor.demo.gateway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.konveyor.demo.gateway.model.Customer;
import io.konveyor.demo.gateway.model.Order;
import io.konveyor.demo.gateway.model.OrderItem;
import io.konveyor.demo.gateway.model.Product;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;

@RunWith(SpringRunner.class)
public class OrderRepositoryTest {
	
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
			properties.setProperty("services.orders.url", "http://orders.svc:8080/orders");
			pspc.setProperties(properties);
			return pspc;
		}
		
		@Bean
		public OrderRepository repository() {
			return new OrderRepository();
		}
	}
	
	@MockBean
	RestTemplate restTemplate;
	
	@Autowired
	OrderRepository repository;
	
	private Order order;
	
	@Before
	public void setup() {
		order = new Order();
		order.setId(new Long(1));
		order.setDate(new GregorianCalendar(2018, 4, 30).getTime());
		
		Product p = new Product();
		p.setId(new Long(1));
		
		Customer c = new Customer();
		c.setId(new Long(1));
		
		OrderItem i = new OrderItem();
		i.setPrice(new BigDecimal(30));
		i.setQuantity(3);
		i.setProduct(p);
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		items.add(i);
		
		order.setItems(items);
		order.setCustomer(c);
	}
	
	@Test
	public void getOrderByIdExistingTest() {
		Mockito.when(restTemplate.getForObject("http://orders.svc:8080/orders/1", Order.class))
			.thenReturn((Order)SerializationUtils.clone(order));
		
		Order found = repository.getOrderById(new Long(1));
		
		assertThat(found).isEqualTo(order);
	}
	
	@Test
	public void getOrderByIdNonExistingTest() {
		Mockito.when(restTemplate.getForObject("http://orders.svc:8080/orders/1", Order.class))
		.thenReturn(null);
		
		Order found = repository.getOrderById(new Long(1));
		
		assertThat(found).isNull();
	}
	
	@Test
	public void getFallbackCustomerTest() {
		assertThat(repository.getFallbackOrder(new Long(1), new Exception())).isNull();
	}
}
