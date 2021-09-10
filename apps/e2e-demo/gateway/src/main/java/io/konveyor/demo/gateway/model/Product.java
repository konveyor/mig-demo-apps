package io.konveyor.demo.gateway.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Product implements Serializable{
	
	private static final long serialVersionUID = 1647872601694471121L;
	private Long id;
	private String name;
	private String description;
}
