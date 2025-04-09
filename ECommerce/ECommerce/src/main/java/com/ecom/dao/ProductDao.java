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
public interface ProductDao extends CrudRepository<Product, Long>{
//
	public Page findAll(Pageable pageable);
	
	public List<Product>  findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
		String key1, String key2, Pageable pageable);
//	

//	 Page<Product> findBySellername(String username, Pageable pageable);

	public  Page<Product> findBySellername(String username, Pageable pageable) ;
}
