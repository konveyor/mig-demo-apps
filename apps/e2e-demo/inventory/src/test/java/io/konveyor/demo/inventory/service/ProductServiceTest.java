package io.konveyor.demo.inventory.service;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.konveyor.demo.inventory.model.Product;
import io.konveyor.demo.inventory.repository.ProductRepository;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;


@QuarkusTest
public class ProductServiceTest {

	@Inject
	ProductService service;
	
	@InjectMock
	ProductRepository productRepository;
	
	Product product;
	
	@BeforeEach
	void initProduct() {
		product = new Product();
		product.setId(1L);
		product.setName("Test");
		product.setDescription("Test Product");
	}
	
	@Test
	public void findByIdExistingTest() {
		
		when(productRepository.findById(1L)).thenReturn(product);
		
		Product p = service.findById(1L);
		assertThat(p.getId(), equalTo(1L));
		assertThat(p.getName(), equalTo("Test"));
		assertThat(p.getDescription(), equalTo("Test Product"));
	}
	
	@Test
	public void findByIdNonExistingTest() {
		
		when(productRepository.findById(2L)).thenReturn(null);
		
		Product p = service.findById(2L);
		assertThat(p, is(nullValue()));
	}
	
}
