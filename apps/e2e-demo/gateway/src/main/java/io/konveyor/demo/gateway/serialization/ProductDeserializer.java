package io.konveyor.demo.gateway.serialization;

import java.io.IOException;

import io.konveyor.demo.gateway.model.Product;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ProductDeserializer extends StdDeserializer<Product>{

	private static final long serialVersionUID = 8793658736527680362L;

	public ProductDeserializer() {
		this(null);
	}
	
	public ProductDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Product deserialize(JsonParser parser, DeserializationContext deserializer)
			throws IOException, JsonProcessingException {
		Product p = new Product();
		p.setId(parser.readValueAs(Long.class));
		return p;
	}
}
