package io.konveyor.demo.gateway.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class OrderSummary implements Serializable{
	
	private static final long serialVersionUID = 5290825002214978384L;
	private long id;
	private String customerName;
	private Date date;
	private double totalAmmount;

}
