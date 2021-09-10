package io.konveyor.demo.gateway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.konveyor.demo.gateway.model.Customer;
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
public class CustomerRepositoryTest {
	
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
			properties.setProperty("services.customers.url", "http://customers.svc:8080/customers");
			pspc.setProperties(properties);
			return pspc;
		}
		
		@Bean
		public CustomerRepository repository() {
			return new CustomerRepository();
		}
	}
	
	@MockBean
	RestTemplate restTemplate;
	
	@Autowired
	CustomerRepository repository;
	
	private Customer customer;
	
	@Before
	public void setup() {
		customer = new Customer();
		customer.setId(new Long(1));
		customer.setName("Test Customer");
		customer.setSurname("Test Customer");
		customer.setUsername("testcustomer");
		customer.setZipCode("28080");
		customer.setCountry("Spain");
		customer.setCity("Madrid");
		customer.setAddress("Test Address");
	}
	
	@Test
	public void getCustomerByIdExistingTest() {
		Mockito.when(restTemplate.getForObject("http://customers.svc:8080/customers/1", Customer.class))
			.thenReturn((Customer)SerializationUtils.clone(customer));
		
		Customer found = repository.getCustomerById(new Long(1));
		assertThat(found).isEqualTo(customer);
	}
	
	@Test(expected = RuntimeException.class)
	public void getCustomerByIdNonExistingTest() {
		
		Mockito.when(restTemplate.getForObject("http://customers.svc:8080/customers/1", Customer.class))
		.thenReturn(null);
		
		repository.getCustomerById(new Long(1));
	}
	
	@Test
	public void getFallbackCustomerTest() {
		Customer c = new Customer();
		c.setId(new Long(1));
		c.setUsername("Unknown");
		c.setName("Unknown");
		c.setSurname("Unknown");
		c.setAddress("Unknown");
		c.setCity("Unknown");
		c.setCountry("Unknown");
		c.setZipCode("Unknown");
		
		Customer found = repository.getFallbackCustomer(new Long(1), new Exception());
		
		assertThat(found).isEqualTo(c);
	}
}
