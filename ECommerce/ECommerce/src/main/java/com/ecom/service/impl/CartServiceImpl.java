
package com.ecom.service.impl;

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
import com.ecom.proxy.CartProxy;
import com.ecom.service.CartService;
import com.ecom.util.MapperUtil;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartDao cartDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private MapperUtil mapperUtil;

	public CartProxy addtoCart(Long productId) {
		System.out.println("add to cart service");
		Product product = productDao.findById(productId).orElse(null);
		User user = userDao.findById(getCurrentUsername()).orElse(null);

		if (product != null && user != null) {
			List<Cart> existingCartItems = cartDao.findByUserAndProduct(user, product);
			if (!existingCartItems.isEmpty()) {
				// Product already in cart, could update quantity if needed
				return mapperUtil.convertValue(existingCartItems.get(0), CartProxy.class);

			} else {
				// Add new product to cart
				Cart newCartItem = new Cart();
				newCartItem.setProduct(product);
				newCartItem.setUser(user);
				cartDao.save(newCartItem); 
				return mapperUtil.convertValue(newCartItem, CartProxy.class);
			}
		}
		return null;
	}

	public void deleteCartItem(Long cartId) {
		cartDao.deleteById(cartId);
	}

	public List<CartProxy> getCartDetails() {
		String username = getCurrentUsername();
		User user = userDao.findById(username).orElse(null);
		if (user != null) {
			return mapperUtil.convertList(cartDao.findByUser(user), CartProxy.class);
		}
		return null;
	}

	private String getCurrentUsername() {

		return JwtRequestFilter.CURRENT_USER;

	}
}
