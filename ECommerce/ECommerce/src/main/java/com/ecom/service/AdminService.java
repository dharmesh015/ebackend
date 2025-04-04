package com.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dao.RoleDao;
import com.ecom.dao.UserDao;
import com.ecom.entity.Product;
import com.ecom.entity.User;

@Service
public class AdminService {

	
	
	@Autowired
	private UserDao userDao;
	
	
	@Autowired
	private RoleDao roleDao;
	
	public List<User> getAllUser(){
		return (List<User>) userDao.findAll();
	}


	public Page<Product> getAllProductsPageWise(PageRequest pageable) {
		System.err.println("pagewise service");
//    	System.err.println(userDao.findAll(pageable));
    	 return userDao.findAll(pageable); 
     
	}


	@Transactional
    public void deleteUser (String userName) {
        User user = userDao.findById(userName).orElseThrow(() -> new RuntimeException("User  not found"));
        
        // Remove the user from the USER_ROLE table
        user.getRole().clear(); // This will remove the associations in the USER_ROLE table
        
        // Now delete the user
        userDao.delete(user);
    }
}
