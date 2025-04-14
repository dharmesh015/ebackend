package com.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.entity.Cart;
import com.ecom.proxy.CartProxy;
import com.ecom.service.CartService;
import com.ecom.service.impl.CartServiceImpl;

@RestController
public class CartController {

	@Autowired
	private CartService cartService;

	@PreAuthorize("hasRole('User')")
	@GetMapping({ "/addToCart/{productId}" })
	public CartProxy addTocart(@PathVariable(name = "productId") Long productId) {
		System.err.println("add cart controller" + productId);
		return cartService.addtoCart(productId);

	}
 
	@DeleteMapping({ "/deleteCartItem/{cartId}" })
	public void deleteCartItem(@PathVariable(name = "cartId") Long cartId) {
		cartService.deleteCartItem(cartId);
	}

	@PreAuthorize("hasRole('User')")
	@GetMapping({ "/getCartDetails" })
	public List<CartProxy> getCartDetails() {
		return cartService.getCartDetails();

	}

}
