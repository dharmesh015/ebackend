package com.ecom.service.impl;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.dao.RoleDao;
import com.ecom.dao.UserDao;
import com.ecom.entity.Role;
import com.ecom.entity.User;
import com.ecom.proxy.UserProxy;
import com.ecom.service.EmailService;
import com.ecom.service.UserService;
import com.ecom.util.MapperUtil;

import jakarta.annotation.PostConstruct;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MapperUtil mapper;
	
	@Autowired
	private EmailService emailservice;

	@PostConstruct
	public void initRoleAndUser() {
		try {
			Role sellerRole = new Role();
			sellerRole.setRoleName("Seller");
			sellerRole.setRoleDescription("Seller role");
			roleDao.save(sellerRole);

			Role adminRole = new Role();
			adminRole.setRoleName("Admin");
			adminRole.setRoleDescription("Admin role");
			roleDao.save(adminRole);

			Role userRole = new Role();
			userRole.setRoleName("User");
			userRole.setRoleDescription("Default role for newly created record");
			roleDao.save(userRole);

			User sellerUser = new User();
			sellerUser.setUserName("dharmesh123");
			sellerUser.setUserPassword(getEncodedPassword("seller"));
			sellerUser.setUserFirstName("dharmesh");
			sellerUser.setUserLastName("gelatar");
			sellerUser.setAddress("ahmedabad");
			sellerUser.setEmail("dharmesh@gmail.com");
			sellerUser.setMobileNumber("9069060653");
			Set<Role> roles = new HashSet<>();
			roles.add(sellerRole);
			sellerUser.setRole(roles);
			userDao.save(sellerUser);

			User adminUser = new User();
			adminUser.setUserName("deep123");
			adminUser.setUserPassword(getEncodedPassword("admin"));
			adminUser.setUserFirstName("deep");
			adminUser.setUserLastName("patel");
			adminUser.setAddress("ahmedabad");
			adminUser.setEmail("deep@gmail.com");
			adminUser.setMobileNumber("9965060653");
			Set<Role> adminRoles = new HashSet<>();
			adminRoles.add(adminRole);
			adminUser.setRole(adminRoles);
			userDao.save(adminUser);
		} catch (Exception e) {
			// Log the error
			System.err.println("Error initializing roles and users: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public String registerNewUser(UserProxy usero) {
		
		
		if(userDao.findByEmail(usero.getEmail()) != null) {
			System.err.println("EmailExist");
			return "EmailExist";
		}

	
		User user = mapper.convertValue(usero, User.class);
		user.setUserPassword(getEncodedPassword(user.getUserPassword()));

		User savedUser = userDao.save(user);

		Role role = roleDao.findById("User").orElseThrow(() -> new RuntimeException("Role not found"));
		Set<Role> userRoles = new HashSet<>();
		userRoles.add(role);
		savedUser.setRole(userRoles);

		User userobj = userDao.save(savedUser);
		return "register";
	}

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	@Override
	public void updateUserRole(String userName, String roleobj) {
		User byUserName = userDao.findByUserName(userName).get();
		Role role = roleDao.findById(roleobj).orElseThrow(() -> new RuntimeException("Role not found"));
		Set<Role> userRoles = new HashSet<>();
		userRoles.add(role);
		byUserName.setRole(userRoles);
		
		userDao.save(byUserName);
		
		emailservice.sendEmailToUser(userName, roleobj);
	}
}