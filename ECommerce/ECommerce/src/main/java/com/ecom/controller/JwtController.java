package com.ecom.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.ecom.proxy.UserProxy;
import com.ecom.service.JwtService;
import com.ecom.service.impl.EmailserviceImpl;
import com.ecom.service.impl.TokenService;
import com.ecom.service.impl.UserService;

@RestController
@CrossOrigin
public class JwtController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService Service;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private EmailserviceImpl emailService;

	@PostMapping("/authenticate")
	public JwtResponse createJwtToken(@RequestBody JwtRequest user) throws Exception {
		System.out.println("name" + user.getUserName());
		System.out.println("Received UserName: " + user.getUserName());
		System.out.println("Received Password: " + user.getUserPassword());

		if (user.getUserName() == null || user.getUserPassword() == null) {
			System.out.println("null data");
		}
		return jwtService.createJwtToken(user);
	}

	@GetMapping({ "/getdata/{token}" })
	public UserProxy getdata(@PathVariable("token") String token) {
		return jwtService.getdata(token);

	}

	@PostMapping("/registerNewUser")
	public UserProxy registerNewUser(@RequestBody UserProxy user) {
		System.out.println("controler");
		return Service.registerNewUser(user);
	}

	@GetMapping("/validate-token/{token}")
	public ResponseEntity<?> validateToken(@PathVariable(value = "token") String token) {
		System.err.println("token--" + token);
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

}
