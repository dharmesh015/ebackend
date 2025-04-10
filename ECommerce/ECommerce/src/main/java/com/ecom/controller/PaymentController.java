package com.ecom.controller;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

  private RazorpayClient razorpayClient;

  public PaymentController() throws Exception {
    this.razorpayClient = new RazorpayClient("your-razorpay-key", "your-razorpay-secret");
  }

  @PostMapping("/create-order")
  public String createOrder(@RequestBody PaymentRequest paymentRequest) {
    try {
      JSONObject orderRequest = new JSONObject();
      orderRequest.put("amount", paymentRequest.getAmount() * 100); // in paise
      orderRequest.put("currency", "INR");
      orderRequest.put("receipt", "order_receipt");

      Order order = razorpayClient.orders.create(orderRequest);
      return order.toString();
    } catch (Exception e) {
      return "Error creating order: " + e.getMessage();
    }
  }
}

class PaymentRequest {
  private double amount;
  public double getAmount() {
    return amount;
  }
  public void setAmount(double amount) {
    this.amount = amount;
  }
}
