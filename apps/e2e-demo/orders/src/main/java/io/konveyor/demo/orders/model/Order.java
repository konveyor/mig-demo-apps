package io.konveyor.demo.orders.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.ToString;

@ToString(exclude = "items")
@Data
@Entity
@Table(name = "orders")
public class Order implements Serializable {

	private static final long serialVersionUID = -2539346800640467934L;
	
	@Id
    @SequenceGenerator(
            name = "orderSequence",
            sequenceName = "order_id_seq",
            allocationSize = 1,
            initialValue = 7)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSequence")
	private Long id;
	
	@Column(name = "customer_uid")
	private Long customerUID;
	
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone="CET")
	@Column(name = "order_date")
	private Date date;
	
	@JsonManagedReference
	@OneToMany(
	        mappedBy = "order", 
	        cascade = CascadeType.ALL, 
	        orphanRemoval = true)
	private List<OrderItem> items;
	

}
