package com.redhat.productcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redhat.productcatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	public Product findByName(String name);


}
