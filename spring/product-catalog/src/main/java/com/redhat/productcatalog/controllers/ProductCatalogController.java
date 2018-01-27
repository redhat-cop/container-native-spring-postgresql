package com.redhat.productcatalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.redhat.productcatalog.entities.Product;
import com.redhat.productcatalog.services.ProductCatalogService;

@RestController
@RequestMapping(value = "/product")
public class ProductCatalogController {
		
	@Autowired
	ProductCatalogService productService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Product saveProduct(@RequestBody Product product) {
		return productService.addProduct(product);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<Product> showProductList() {
		return productService.showProductList();
	}

	@GetMapping(value="/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product getProduct(@PathVariable("itemId") long itemId, @RequestHeader("Authorization") String token) {
		return productService.getProduct(itemId);
	}
	
	@PutMapping(value="/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
	public Product updateProduct(@PathVariable("itemId") long itemId, @RequestBody Product product) {
		return productService.updateProduct(itemId, product);
	}
	
    @ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(value="/{itemId}")
	public void deleteProduct(@PathVariable("itemId") long itemId) {
		productService.deleteProduct(itemId);
	}
}
