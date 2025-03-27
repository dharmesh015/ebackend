package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.entity.JwtRequest;
import com.ecom.entity.JwtResponse;
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
}
