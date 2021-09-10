package io.konveyor.demo.gateway.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable{

	private static final long serialVersionUID = -647167746768043097L;
	private Long id;
	private String username;
	private String name;
	private String surname;
	private String address;
	private String zipCode;
	private String city;
	private String country;
	
}
