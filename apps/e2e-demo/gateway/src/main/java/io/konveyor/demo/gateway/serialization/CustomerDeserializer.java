package io.konveyor.demo.gateway.serialization;

import java.io.IOException;

import io.konveyor.demo.gateway.model.Customer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomerDeserializer extends StdDeserializer<Customer>{

	private static final long serialVersionUID = 8793658736527680362L;

	
	public CustomerDeserializer() {
		this(null);
	}
	
	
	public CustomerDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Customer deserialize(JsonParser parser, DeserializationContext deserializer)
			throws IOException, JsonProcessingException {
		Customer c = new Customer();
		c.setId(parser.readValueAs(Long.class));
		return c;
	}
}
