package com.ecom.service;

import java.util.List;
import java.util.Optional;
import com.ecom.util.MapperUtil;
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
import com.ecom.proxy.UserProxy;

@Service
public class AdminService {

    private final MapperUtil mapperUtil;

	
	
	@Autowired
	private UserDao userDao;
	
	
	@Autowired
	private RoleDao roleDao;
	
	
	@Autowired
	private UserImageDao imageDao;


    AdminService(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }
	
	
	public List<User> getAllUser(){
		return (List<User>) userDao.findAll();
	}


	public Page<UserProxy> getAllUsersPageWise(PageRequest pageable) {
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
	
	
	public String updateUser(UserProxy user) {
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


	public UserProxy getuser(String name) {
		Optional<User> userdata = userDao.findById(name);
		return  mapperUtil.convertValue( userdata.get(),UserProxy.class);
	}
}
