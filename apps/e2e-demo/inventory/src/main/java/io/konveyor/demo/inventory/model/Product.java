package io.konveyor.demo.inventory.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Entity
@Table(name = "products")
@RegisterForReflection
public class Product extends PanacheEntityBase {
	
	@Id
    @SequenceGenerator(
            name = "productsSequence",
            sequenceName = "products_id_seq",
            allocationSize = 1,
            initialValue = 7)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productsSequence")
	private Long id;
	
	@Column(length = 60)
	private String name;
	
	@Column(length = 255)
	private String description;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
	
}
