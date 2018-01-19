package com.redhat.productcatalog.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redhat.productcatalog.entities.Product;
import com.redhat.productcatalog.interfaces.ProductCatalog;
import com.redhat.productcatalog.repositories.ProductRepository;

@Service
public class ProductCatalogService implements ProductCatalog  {

	@Autowired
	ProductRepository repository;

	@Override
	public Product addProduct(Product product) {
        verifyCorrectPayload(product);
		return repository.save(product);
	}
	
	@Override
	public List<Product> showProductList() {
		return repository.findAll();
	}

	@Override
	public Product getProduct(long itemId) {
        verifyProductExists(itemId);
		return repository.findOne(itemId);
	}

	@Override
	public Product updateProduct(long itemId, Product product) {
		verifyProductExists(itemId);
        verifyCorrectPayload(product);
        product.setItemId(itemId);
		return repository.save(product);
		
	}
	
	@Override
	public void deleteProduct(long itemId) {
        verifyProductExists(itemId);
        repository.delete(itemId);		
	}
    private void verifyProductExists(long itemId) {
        if (!repository.exists(itemId)) {
            throw new RuntimeException(String.format("Product with id=%d was not found", itemId));
        }
    }

    private void verifyCorrectPayload(Product product) {
        if (Objects.isNull(product) || product.getName().isEmpty()) {
            throw new RuntimeException("Product details can't be empty");
        }

        if (!Objects.isNull(product.getItemId())) {
            throw new RuntimeException("Id field must be generated");
        }
    }
}