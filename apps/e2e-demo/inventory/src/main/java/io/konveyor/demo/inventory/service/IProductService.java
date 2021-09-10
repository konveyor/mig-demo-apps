package io.konveyor.demo.inventory.service;

import java.util.List;

import io.konveyor.demo.inventory.model.Product;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

public interface IProductService {
	
	public Product findById(Long id);
	
	public List<Product> findAll(Page page, Sort sort);
}
