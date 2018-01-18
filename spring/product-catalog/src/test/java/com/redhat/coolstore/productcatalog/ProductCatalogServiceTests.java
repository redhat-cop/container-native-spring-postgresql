package com.redhat.coolstore.productcatalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.redhat.productcatalog.ProductCatalogApplication;
import com.redhat.productcatalog.entities.Product;
import com.redhat.productcatalog.services.ProductCatalogService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ProductCatalogApplication.class})
//@DataJpaTest
public class ProductCatalogServiceTests {

	@Autowired
	ProductCatalogService catalog;

	
	@Test
	public void testFindAll() {
		List<Product> productList = catalog.showProductList();
		assertEquals(productList.size(), 8);
	}
	
	@Test(expected=RuntimeException.class)
	public void testSaveAndDeleteProduct() {
		
		Product newProduct = new Product();
		newProduct.setName("Test Prod");
		newProduct.setDescription("This is a description");
		newProduct.setPrice(10.00d);
		
		Product product = catalog.addProduct(newProduct);
		long id = product.getItemId();
		
		assertNotNull(catalog.getProduct(id));
		
		catalog.deleteProduct(id);
		
		catalog.getProduct(id);
	}

}
