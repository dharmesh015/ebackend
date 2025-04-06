
package com.ecom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.configuration.JwtRequestFilter;
import com.ecom.dao.CartDao;
import com.ecom.dao.ProductDao;
import com.ecom.dao.UserDao;
import com.ecom.entity.Cart;
import com.ecom.entity.Product;
import com.ecom.entity.User;

@Service
public class CartService {

    @Autowired
    private CartDao cartDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private UserDao userDao;
    
    public Cart addtoCart(Long productId) {
    	System.out.println("add to cart service");
        Product product = productDao.findById(productId).orElse(null);
        User user = userDao.findById(getCurrentUsername()).orElse(null);
        
        if (product != null && user != null) {
            // Check if product already exists in cart
            List<Cart> existingCartItems = cartDao.findByUserAndProduct(user, product);
            if (!existingCartItems.isEmpty()) {
                // Product already in cart, could update quantity if needed
                return existingCartItems.get(0);
            } else {
                // Add new product to cart
                Cart newCartItem = new Cart();
                newCartItem.setProduct(product);
                newCartItem.setUser(user);
                return cartDao.save(newCartItem);
            }
        }
        return null;
    }
    
    public void deleteCartItem(Long cartId) {
        cartDao.deleteById(cartId);
    }
    
    public List<Cart> getCartDetails() {
        String username = getCurrentUsername();
        User user = userDao.findById(username).orElse(null);
        if (user != null) {
            return cartDao.findByUser(user);
        }
        return null;
    }
    
    private String getCurrentUsername() {
        // This should be implemented based on your security setup
        // Usually this would get the authenticated user from the Security Context
        return JwtRequestFilter.CURRENT_USER;
        		// Replace with actual implementation
    }
}
