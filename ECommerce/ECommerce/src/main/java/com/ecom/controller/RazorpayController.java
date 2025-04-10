package com.ecom.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;


@RestController
public class RazorpayController {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    
	@PreAuthorize("hasRole('User')")
    @PostMapping("/createRazorpayOrder")
    public ResponseEntity<Map<String, String>> createRazorpayOrder(@RequestBody Map<String, Object> payload) {
        try {
            Double amount = Double.parseDouble(payload.get("amount").toString());
            
            // Initialize Razorpay client with your API key and secret
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            
            // Create order JSON
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); // Convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_" + System.currentTimeMillis());
            
            // Create the order
            Order order = razorpay.orders.create(orderRequest);
            
            // Return order ID to frontend
            Map<String, String> response = new HashMap<>();
            response.put("id", order.get("id"));
            System.out.println(response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}