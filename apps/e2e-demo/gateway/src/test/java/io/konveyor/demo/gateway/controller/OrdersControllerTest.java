package io.konveyor.demo.gateway.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.konveyor.demo.gateway.model.Customer;
import io.konveyor.demo.gateway.model.Order;
import io.konveyor.demo.gateway.model.OrderItem;
import io.konveyor.demo.gateway.model.Product;
import io.konveyor.demo.gateway.service.OrdersService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(
		  locations = "classpath:application-test.properties")
public class OrdersControllerTest {
	
	@Value("${local.server.port}")
    private int port;
	
	@MockBean
	private OrdersService service;
	
	private Order order;
	
	private OrderItem item;
	
	private Product product;
	
	private Customer customer;
	
	@Before
	public void setUp() {
		order = new Order();
		order.setId(new Long(1));
		order.setDate(new GregorianCalendar(2018, 4, 30).getTime());
		
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
		
		RestAssured.baseURI = String.format("http://localhost:%d/orders", port);
	}
	
	@Test
	public void getByIdExisting() {
		Mockito.when(service.getById(new Long (1)))
	      .thenReturn(order);
		
		when().get(new Long(1).toString())
			.then()
			.statusCode(200)
			.body("id", is(1))
			.body("date", is("30-05-2018"))
			.body("customer.id", is(1))
			.body("customer.name", is("Test Customer"))
			.body("customer.surname", is("Test Customer"))
			.body("customer.username", is("testcustomer"))
			.body("customer.zipCode", is("28080"))
			.body("customer.city", is("Madrid"))
			.body("customer.country", is("Spain"))
			.body("customer.address", is("Test Address"))
			.body("items.size()", is(1))
			.body("items.get(0).quantity", is(3))
			.body("items.get(0).price", is(30))
			.body("items.get(0).product.id", is(1))
			.body("items.get(0).product.name", is("Test Product"))
			.body("items.get(0).product.description", is("Test Description"));
	}
	
	@Test
	public void getByIdNonExisting() {
		Mockito.when(service.getById(new Long (1)))
	      .thenReturn(null);
		
		when().get(new Long(1).toString())
		.then()
		.statusCode(404);
	}
}
