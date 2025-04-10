package com.ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.proxy.CartProxy;


public interface CartService {
	CartProxy addtoCart(Long productId);
    void deleteCartItem(Long cartId);
    List<CartProxy> getCartDetails();
}
