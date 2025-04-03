package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.service.Emailservice;

@RestController
@CrossOrigin
public class EmailConreoller {

	
	@Autowired
	public Emailservice eservice;
	
	@GetMapping("/sendEmail")
	public String emailsend() {
		 eservice.sendEmail();
		 return "successfully send";
	}
} 
