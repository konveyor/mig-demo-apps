package io.konveyor.demo.orders.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem implements Serializable {

	private static final long serialVersionUID = 2888227823793439361L;
	@Id
    @SequenceGenerator(
            name = "orderItemSequence",
            sequenceName = "orderitem_id_seq",
            allocationSize = 1,
            initialValue = 7)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderItemSequence")
	private Long id;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
	private Order order;
	
	@Column(name = "product_uid")
	private Long productUID;
	
	private Integer quantity;
	
	private BigDecimal price;

}
