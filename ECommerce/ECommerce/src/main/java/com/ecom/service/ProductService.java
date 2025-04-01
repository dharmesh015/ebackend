package com.ecom.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dao.OrderDetailDao;
import com.ecom.dao.ProductDao;
import com.ecom.entity.Product;



@Service
public class ProductService {
    
    @Autowired
    private ProductDao productDao;
    
    
    @Autowired
    private OrderDetailDao orderdetaildao;

    @Transactional
    public Product addNewProduct(Product product) {
        // Ensure the product is saved correctly
//    	System.out.println(product.);
        return productDao.save(product);        
    }

	
    public List<Product> getAllProducts() {
       
            return (List<Product>) productDao.findAll(); // Use getContent() to get the list of products
       
    }


    @Transactional
    public void deleteProductById(Long productId) {
    	
    	orderdetaildao.deleteById(productId);
        productDao.deleteById(productId);
    }
    
    
    public Product updateProduct(Product product) {
        // Check if the product exists
        if (productDao.existsById(product.getProductId())) {
            return productDao.save(product); // Save the updated product
        } else {
            throw new RuntimeException("Product not found with id: " + product.getProductId());
        }
    }
    
    public Product getProductById(Long productId) {
        // Logic to retrieve the product from the database
        // For example, using a repository:
        return productDao.findById(productId).orElse(null);
    }
    
    public List<Product> getProductDetails(boolean isSingeProductCheckout, Integer productId) {
    	
		if(isSingeProductCheckout && productId != 0) {
			List<Product> list= new ArrayList<>();
			Long id=(long)productId;
			Product product = productDao.findById(id).get();
			list.add(product);
			System.out.println(list.getFirst().getProductDescription());
			return list;
		}else {
		
//			String username = JwtRequestFilter.CURRENT_USER;
//			User user = userDao.findById(username).get();
//			List<Cart>  carts= cartDao.findByUser(user);
//			
//			return carts.stream().map(x -> x.getProduct()).collect(Collectors.toList());
			
		}
		
		return new ArrayList<>();

}
    public Page<Product> getAllProductsPageWise(Pageable pageable) {
    	System.err.println("pagewise service");
    	System.err.println(productDao.findAll(pageable));
    	 return productDao.findAll(pageable); 
     
    }

}