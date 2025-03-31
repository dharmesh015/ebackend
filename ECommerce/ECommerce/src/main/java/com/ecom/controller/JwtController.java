package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.entity.JwtRequest;
import com.ecom.entity.JwtResponse;
import com.ecom.entity.User;
import com.ecom.service.JwtService;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/authenticate")
    public JwtResponse createJwtToken(@RequestBody JwtRequest user) throws Exception {
    	System.out.println("name"+user.getUserName());
    	 System.out.println("Received UserName: " + user.getUserName());
         System.out.println("Received Password: " + user.getUserPassword());

         if (user.getUserName() == null || user.getUserPassword() == null) {
            System.out.println("null data");
         }
        return jwtService.createJwtToken(user);
    }
    
//    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getdata/{token}"})
    public User getdata(@PathVariable("token") String token) {
    	return jwtService.getdata(token);
    	
    }
}
