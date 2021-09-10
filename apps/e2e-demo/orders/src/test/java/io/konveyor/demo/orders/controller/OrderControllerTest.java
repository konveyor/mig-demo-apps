package io.konveyor.demo.orders.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.konveyor.demo.orders.model.Order;
import io.konveyor.demo.orders.model.OrderItem;
import io.konveyor.demo.orders.service.OrderService;
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
public class OrderControllerTest {
	
	@Value("${local.server.port}")
    private int port;
	
	@MockBean
	private OrderService service;
	
	private Order order;
	
	@Before
	public void setUp() {
		OrderItem item = new OrderItem();
		item.setId(new Long(1));
		item.setPrice(new BigDecimal(30));
		item.setProductUID(new Long (1));
		item.setQuantity(3);
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		items.add(item);
		
		order = new Order();
		order.setId(new Long(1));
		order.setCustomerUID(new Long(1));
		order.setDate(new GregorianCalendar(2018, 4, 30).getTime());
		order.setItems(items);
		
		item.setOrder(order);
		
		RestAssured.baseURI = String.format("http://localhost:%d/orders", port);
	}
	
	@Test
	public void getByIdExisting() {
		Mockito.when(service.findById(new Long (1)))
	      .thenReturn(order);
		
		when().get(new Long(1).toString())
			.then()
			.statusCode(200)
			.body("id", is(1))
			.body("customerUID", is(1))
			.body("date", is("30-05-2018"))
			.body("items.size()", is(1))
			.body("items.get(0).id", is(1))
			.body("items.get(0).productUID", is(1))
			.body("items.get(0).quantity", is(3))
			.body("items.get(0).price", is(30));
	}
	
	@Test
	public void getByIdNonExisting() {
		Mockito.when(service.findById(new Long (2)))
	      .thenReturn(null);
		when().get(new Long(2).toString())
			.then()
			.statusCode(404);
	}
}
