package com.ecom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.configuration.JwtRequestFilter;
import com.ecom.dao.CartDao;
import com.ecom.dao.OrderDetailDao;
import com.ecom.dao.ProductDao;
import com.ecom.dao.UserDao;
import com.ecom.entity.Cart;
import com.ecom.entity.OrderDetail;
import com.ecom.entity.Product;
import com.ecom.entity.User;



@Service
public class ProductService {
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private UserDao userdao;
    
    @Autowired
    private CartDao cartdao;
    
    
    @Autowired
    private OrderDetailDao orderdetaildao;
    @Autowired
    private OrderDetailService orderDetailService;

    @Transactional
    public Product addNewProduct(Product product) {

        return productDao.save(product);        
    }

	
    public List<Product> getAllProducts() {
       
            return (List<Product>) productDao.findAll(); // Use getContent() to get the list of products
       
    }


    @Transactional
    public void deleteProductById(Long productId) {
    
        orderDetailService.deleteOrderDetailsByProductId(productId); // Delete related orders first
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
        Optional<Product> product = productDao.findById(productId);
        return product.orElse(null);
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
//    	 
     
    }
    public Page<Product> getAllProductsPageWiseByUser(Pageable pageable) {
    	System.err.println("pagewise service");
    	System.err.println(productDao.findAll(pageable));
//    	 return productDao.findAll(pageable); 
    	 return productDao.findBySellername(getCurrentUsername(), pageable); 
    }
    


	
	public Page<Product> getproductbyusername(String username, Pageable pageable) {
	    User user = userdao.findByUserName(username).get();
	    Page<Product> products = productDao.findBySellername(username, pageable);
	    System.err.println("Fetching products for user name: " + username);

	   return products;

}
	public List<Cart> getCartDetails() {
        String username = getCurrentUsername();
        User user = userdao.findById(username).orElse(null);
        if (user != null) {
            return cartdao.findByUser(user);
        }
        return null;
    }
    
    private String getCurrentUsername() {
    
        return JwtRequestFilter.CURRENT_USER; 
    }

}