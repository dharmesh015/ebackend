package com.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dao.ProductDao;
import com.ecom.entity.Product;



@Service
public class ProductService {
    
    @Autowired
    private ProductDao productDao;

    @Transactional
    public Product addNewProduct(Product product) {
        // Ensure the product is saved correctly
//    	System.out.println(product.);
        return productDao.save(product);        
    }

	
    public List<Product> getAllProducts() {
       
            return productDao.findAll(); // Use getContent() to get the list of products
       
    }

	@Transactional
    public void deleteProductById(Long productId) {
        productDao.deleteById(productId);
    }

}
