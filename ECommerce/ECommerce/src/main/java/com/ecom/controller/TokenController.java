package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.service.Emailservice;
import com.ecom.service.TokenService;
//import com.ecom.service.EmailService;

import java.util.HashMap;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "*")
public class TokenController {

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private Emailservice emailService;
    
    @GetMapping("/validate-token/{token}")
    public ResponseEntity<?> validateToken(@PathVariable(value = "token") String token) {
    	System.err.println("token--"+token);
//    	System.err.println("email--"+tokenService.getemail(token));
        String email = tokenService.validateToken(token);
        System.err.println(email);
        if (email != null) {
            Map<String, String> response = new HashMap<>();
            response.put("email", email);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }
//    @GetMapping("/reset-password/{token}/{newPassword}")
//    public ResponseEntity<String> resetPassword(@PathVariable ("token") String token,@PathVariable ("newPassword") String newPassword) {
//    	System.err.println("reset-passwor-"+newPassword);
//        boolean result = emailService.resetPassword(email,newPassword);
//        
//        if (result) {
//            return ResponseEntity.ok("Password updated successfully");
//        } else {
//            return ResponseEntity.badRequest().body("Failed to update password");
//        }
//    }
    @GetMapping("/reset-password/{token}/{newPassword}")
    public ResponseEntity<String> resetPassword(@PathVariable ("token") String token,@PathVariable ("newPassword") String newPassword) {
        // First validate the token
        String email = tokenService.validateToken(token);
        
        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
        
        // If token is valid, reset the password
        boolean result = emailService.resetPassword(email, newPassword);
        
        if (result) {
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update password");
        }
    }
    
   
    
    
}
