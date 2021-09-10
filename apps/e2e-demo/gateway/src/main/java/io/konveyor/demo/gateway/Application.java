package io.konveyor.demo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;

@SpringBootApplication(exclude = {
	    DataSourceAutoConfiguration.class, 
	    DataSourceTransactionManagerAutoConfiguration.class, 
	    HibernateJpaAutoConfiguration.class
	})
@EnableCircuitBreaker
public class Application
{
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
    }
    
	
	@Bean
    public RestTemplate restTemplate() {   	
    	return new RestTemplate();
    }
    
    @Bean
    public HystrixCommandAspect hystrixAspect() {
      return new HystrixCommandAspect();
    }
	
}
