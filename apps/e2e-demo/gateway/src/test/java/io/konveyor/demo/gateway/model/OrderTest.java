package io.konveyor.demo.gateway.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OrderTest {
	
	@Test
	public void getTotalAmmountItemsNotNullTest() {
		Order o = new Order();
		
		OrderItem i1 = new OrderItem();
		i1.setPrice(new BigDecimal(20));
		i1.setQuantity(1);
		
		OrderItem i2 = new OrderItem();
		i2.setPrice(new BigDecimal(30));
		i2.setQuantity(2);
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		items.add(i1);
		items.add(i2);
		
		o.setItems(items);
		
		assertThat(o.getTotalAmmount()).isEqualTo(new BigDecimal(80));		
		
	}
	
	@Test
	public void getTotalAmmountItemsNullTest() {
		Order o = new Order();
		assertThat(o.getTotalAmmount()).isEqualTo(new BigDecimal(0));
	}
	
	@Test
	public void getTotalAmmountItemsEmptyTest() {
		Order o = new Order();
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		
		o.setItems(items);
		
		assertThat(o.getTotalAmmount()).isEqualTo(new BigDecimal(0));
	}
}
