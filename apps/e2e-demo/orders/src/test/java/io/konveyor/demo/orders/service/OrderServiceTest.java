package io.konveyor.demo.orders.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.konveyor.demo.orders.model.Order;
import io.konveyor.demo.orders.model.OrderItem;
import io.konveyor.demo.orders.repository.OrderRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;

@RunWith(SpringRunner.class)
public class OrderServiceTest {
	
	@TestConfiguration
    static class OrderServiceTestContextConfiguration {
  
        @Bean
        public OrderService orderService() {
            return new OrderService();
        }
        
        @Bean
        public Tracer tracer() {
        	return new Configuration("orders").getTracer();
        }
    }
	
	@MockBean
	private OrderRepository repository;
	
	@Autowired
	private OrderService service;
	
	
	@Test
	public void findByIdExistingTest() {
		//given
		OrderItem item = new OrderItem();
		item.setId(new Long(1));
		item.setPrice(new BigDecimal(30));
		item.setProductUID(new Long (1));
		item.setQuantity(3);
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		items.add(item);
		
		Order o = new Order();
		o.setId(new Long(1));
		o.setCustomerUID(new Long(1));
		o.setDate(new GregorianCalendar(2018, 5, 30).getTime());
		o.setItems(items);
		
		
		//Avoid cyclic references
		Order shallowOrder= new Order();
		shallowOrder.setId(new Long(1));
		
		item.setOrder(shallowOrder);
		
		Mockito.when(repository.findById(new Long (1)))
	      .thenReturn(Optional.of(SerializationUtils.clone(o)));
		
		//when
		Order found = service.findById(new Long(1));
		
		//then
		assertThat(found).isEqualTo(o);
		
	}
	
	@Test
	public void findByIdNonExistingTest() {
		//given
		Mockito.when(repository.findById(new Long (1)))
	      .thenReturn(Optional.empty());
		
		//when
		Order found = service.findById(new Long(1));
				
		//then
		assertThat(found).isNull();	
	}

}
