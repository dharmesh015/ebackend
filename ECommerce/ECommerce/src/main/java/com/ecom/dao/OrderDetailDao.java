package com.ecom.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.entity.OrderDetail;
import com.ecom.entity.Product;
import com.ecom.entity.User;

@Repository
public interface OrderDetailDao extends CrudRepository<OrderDetail, Long>{
	
	public List<OrderDetail> findByUser(User user);
	
	void deleteByUser(User user);
	
//	Page<Product> findByUserName(String userName);
	Page<OrderDetail> findByUser(User user, Pageable pageable);

	public void deleteByProduct_ProductId(Long productId);
	
	List<OrderDetail> findByProduct_Sellername(String sellerName);
	

}
