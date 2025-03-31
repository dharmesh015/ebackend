package com.ecom.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, String> {

	Optional<User> findByUserName(String usrname);
}
