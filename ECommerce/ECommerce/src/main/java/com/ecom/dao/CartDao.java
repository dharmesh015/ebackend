package com.ecom.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.entity.Cart;
import com.ecom.entity.Product;
import com.ecom.entity.User;

@Repository
public interface CartDao extends CrudRepository<Cart, Long> {
	void deleteByUser(User user);

	public List<Cart> findByUser(User user);

	public List<Cart> findByUserAndProduct(User user, Product product);

	void deleteByProduct(Product product);

	void deleteByProduct_ProductId(Long productId);

	List<Cart> findByProduct_ProductId(Long productId);

}
