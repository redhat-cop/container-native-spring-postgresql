package com.redhat.productcatalog.interfaces;

import java.util.List;

import com.redhat.productcatalog.entities.Product;

public interface ProductCatalog {

	/**
	 * @return
	 */
	List<Product> showProductList();
	
	/**
	 * @param itemId
	 */
	void deleteProduct(long itemId);
	
	/**
	 * @param itemId
	 * @return
	 */
	Product getProduct(long itemId);
	
	/**
	 * @param product
	 * @return
	 */
	Product addProduct(Product product);

	/**
	 * @param itemId
	 * @param product
	 * @return
	 */
	Product updateProduct(long itemId, Product product);
}
