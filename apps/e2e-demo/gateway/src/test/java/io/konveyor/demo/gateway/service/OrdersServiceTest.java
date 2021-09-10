package io.konveyor.demo.gateway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.konveyor.demo.gateway.model.Customer;
import io.konveyor.demo.gateway.model.Order;
import io.konveyor.demo.gateway.model.OrderItem;
import io.konveyor.demo.gateway.model.Product;
import io.konveyor.demo.gateway.repository.CustomerRepository;
import io.konveyor.demo.gateway.repository.InventoryRepository;
import io.konveyor.demo.gateway.repository.OrderRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;

@RunWith(SpringRunner.class)
public class OrdersServiceTest {
	
	@TestConfiguration
    static class OrdersServiceTestContextConfiguration {
		@Bean
		public OrdersService ordersService() {
			return new OrdersService();
		}
		
		@Bean
        public Tracer tracer() {
        	return new Configuration("gateway").getTracer();
        }
	}
	
	@Autowired
	OrdersService ordersService;
	
	@MockBean
	private OrderRepository orderRepository;
	
	@MockBean
	private CustomerRepository customerRepository;
	
	@MockBean
	private InventoryRepository inventoryRepository;
	
	private Order order;
	
	private OrderItem item;
	
	private Product product;
	
	private Customer customer;
	
	@Before
	public void setup() {
		order = new Order();
		order.setId(new Long(1));
		order.setDate(new GregorianCalendar(2018, 5, 30).getTime());
		
		customer = new Customer();
		customer.setId(new Long(1));
		customer.setName("Test Customer");
		customer.setSurname("Test Customer");
		customer.setUsername("testcustomer");
		customer.setZipCode("28080");
		customer.setCountry("Spain");
		customer.setCity("Madrid");
		customer.setAddress("Test Address");
		
		product = new Product();
		product.setId(new Long(1));
		product.setName("Test Product");
		product.setDescription("Test Description");
		
		item = new OrderItem();
		item.setPrice(new BigDecimal(30));
		item.setQuantity(3);
		item.setProduct(product);
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		items.add(item);
		
		order.setItems(items);
		order.setCustomer(customer);
	}
	
	@Test
	public void getByIdWithExistingOrderTest() {
		Order o = new Order();
		o.setId(new Long(1));
		o.setDate(new GregorianCalendar(2018, 5, 30).getTime());
		
		Product p = new Product();
		p.setId(new Long(1));
		
		Customer c = new Customer();
		c.setId(new Long(1));
		
		OrderItem i = new OrderItem();
		i.setPrice(new BigDecimal(30));
		i.setQuantity(3);
		i.setProduct(p);
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		items.add(item);
		
		List<OrderItem> detaileditems = new ArrayList<OrderItem>();
		detaileditems.add((OrderItem)SerializationUtils.clone(item));
		
		o.setItems(items);
		o.setCustomer(c);
		
		Mockito.when(orderRepository.getOrderById(new Long(1)))
			.thenReturn(o);
		
		Mockito.when(customerRepository.getCustomerById(new Long(1)))
		.thenReturn((Customer)SerializationUtils.clone(customer));
		
		Mockito.when(inventoryRepository.getProductDetails(items))
		.thenReturn(detaileditems);
		
		Order found = ordersService.getById(new Long(1));
		
		assertThat(found).isEqualTo(order);
	}
	
	@Test
	public void getByIdWithNonExistingOrderTest() {
		Mockito.when(orderRepository.getOrderById(new Long(1)))
		.thenReturn(null);
		
		Order found = ordersService.getById(new Long(1));
		
		assertThat(found).isNull();
	}
}
