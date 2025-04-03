package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.service.Emailservice;

@RestController
@CrossOrigin
public class EmailConreoller {

	
	@Autowired
	public Emailservice emailService;
	
	@GetMapping("/sendEmail/{email}")
    public String sendEmail(@PathVariable ("email")String email) {
		System.out.println(email);
        emailService.sendEmail(email);
        return "Successfully sent";
    }
} 
