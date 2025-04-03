package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.entity.EmailRequest;
import com.ecom.entity.PasswordResetRequest;
import com.ecom.service.Emailservice;


@RestController
@CrossOrigin
public class EmailConreoller {

	
	@Autowired
	public Emailservice emailService;
	
//	@PostMapping("/send-email")
//    public ResponseEntity<String> sendEmail(@RequestBody String email) {
//        String response = emailService.sendEmail(email);
//        return ResponseEntity.ok(response);
//    }
	@PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
		System.out.println("send email--"+request.getEmail());
        String result = emailService.sendPasswordResetEmail(request.getEmail());
        
        if (result.equals("User not found")) {
            return ResponseEntity.ok("User not found");
        } else if (result.equals("Success")) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
//    @GetMapping("/reset-password/{email}/{newPassword}")
//    public ResponseEntity<String> resetPassword(@PathVariable ("email") String email,@PathVariable ("newPassword") String newPassword) {
//    	System.err.println("reset-passwor-"+newPassword);
//        boolean result = emailService.resetPassword(email,newPassword);
//        
//        if (result) {
//            return ResponseEntity.ok("Password updated successfully");
//        } else {
//            return ResponseEntity.badRequest().body("Failed to update password");
//        }
//    }
} 
