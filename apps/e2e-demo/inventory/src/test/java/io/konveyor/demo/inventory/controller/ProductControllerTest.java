package io.konveyor.demo.inventory.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.konveyor.demo.inventory.model.Product;
import io.konveyor.demo.inventory.service.IProductService;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;


@QuarkusTest
public class ProductControllerTest {
	
	@InjectMock
	IProductService productService;
	
	Product product;
	
	@BeforeEach
	void initProduct() {
		product = new Product();
		product.setId(1L);
		product.setName("Test");
		product.setDescription("Test Product");
	}

	@Test
	public void getByIdExisting() {
		
		when(productService.findById(1L)).thenReturn(product);
		
		given()
			.when().get("/products/1")
			.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Test"))
			.body("description", is("Test Product"));		
	}
	
	@Test
	public void getByIdNonExisting() {
		
		when(productService.findById(2L)).thenReturn(null);
		
		given()
			.when().get("/products/2")
			.then()
			.statusCode(404);		
	}

}
