package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.entity.Role;
import com.ecom.entity.User;
import com.ecom.service.RoleService;
import com.ecom.service.UserService;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserService Service;

    @PostMapping("/createNewRole")
    public Role createNewRole(@RequestBody Role role) {
        return roleService.createNewRole(role);
    }
    
    @PostMapping("/registerNewUser")
    public User postMethodName(@RequestBody User user) {
       System.out.println("controler");
    	return Service.registerNewUser(user);
//        return entity;
    }
    
}
