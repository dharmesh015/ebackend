package com.ecom.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.ecom.entity.ImageModel;

public interface ProductImageDao extends JpaRepository<ImageModel ,Long> {

}
