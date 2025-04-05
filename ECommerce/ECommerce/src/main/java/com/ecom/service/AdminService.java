package com.ecom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dao.RoleDao;
import com.ecom.dao.UserDao;
import com.ecom.dao.UserImageDao;
import com.ecom.entity.Product;
import com.ecom.entity.User;

@Service
public class AdminService {

	
	
	@Autowired
	private UserDao userDao;
	
	
	@Autowired
	private RoleDao roleDao;
	
	
	@Autowired
	private UserImageDao imageDao;
	
	
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

        imageDao.deleteByUser(user);
        user.getRole().clear();
        userDao.delete(user);
    }
	
	
	public String updateUser(User user) {
		 User userobj = userDao.findById(user.getUserName()).orElseThrow(() -> new RuntimeException("User  not found"));
	        
		 userobj.setUserPassword(userobj.getUserPassword()); 
		 userobj.setRole(userobj.getRole());
		 userobj.setEmail(user.getEmail());
		 userobj.setAddress(user.getAddress());
		 userobj.setMobileNumber(user.getMobileNumber());
		 userobj.setUserFirstName(user.getUserFirstName());
		 userobj.setUserLastName(user.getUserLastName());
	
	        
	       userDao.save(userobj);
	       return "saved";
	}


	public User getuser(String name) {
		Optional<User> userdata = userDao.findById(name);
		return userdata.get();
	}
}
