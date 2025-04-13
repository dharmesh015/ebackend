package com.ecom.service;

//import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecom.entity.Product;
import com.ecom.proxy.CartProxy;
import com.ecom.proxy.ProductProxy;

public interface ProductService {
	ProductProxy addNewProduct(ProductProxy productProxy);

	List<ProductProxy> getAllProducts();

	void deleteProductById(Long productId);

	ProductProxy updateProduct(ProductProxy productProxy);

	ProductProxy getProductById(Long productId);

	List<ProductProxy> getProductDetails(boolean isSingleProductCheckout, Integer productId);

	Page<ProductProxy> getAllProductsPageWise(Pageable pageable);

	Page<ProductProxy> getAllProductsPageWiseByUser(Pageable pageable);

	Page<ProductProxy> getproductbyusername(String username, Pageable pageable);

	List<CartProxy> getCartDetails();
}
