package com.ecom.controller;

import java.util.List;


import com.ecom.service.impl.TokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.ecom.entity.OrderDetail;
import com.ecom.entity.OrderInput;
import com.ecom.entity.OrderPaymentInput;
import com.ecom.entity.Product;
import com.ecom.proxy.OrderDetailProxy;
import com.ecom.service.OrderDetailService;

@RestController
public class OrderDetailController {

	private final TokenServiceImpl tokenServiceImpl;

	@Autowired
	private OrderDetailService orderDetailService;

	OrderDetailController(TokenServiceImpl tokenServiceImpl) {
		this.tokenServiceImpl = tokenServiceImpl;
	}


	@PostMapping({ "/placeOrder/{issingleProducrCheckout}" })
	public void placeOrder(@PathVariable("issingleProducrCheckout") boolean issingleProducrCheckout,
			@RequestBody OrderInput orderInput) {

		orderDetailService.placeOrder(orderInput, issingleProducrCheckout);

	}


	@PostMapping("/placeOrderWithPayment/{isSingleProductCheckout}")
	public ResponseEntity<String> placeOrderWithPayment(@PathVariable boolean isSingleProductCheckout,
			@RequestBody OrderPaymentInput orderPaymentInput) {
		System.err.println("placed");
		orderDetailService.placeOrderWithPayment(orderPaymentInput, isSingleProductCheckout);
		return ResponseEntity.ok("Order placed successfully with payment");
	}

	@PreAuthorize("hasRole('User')")
	@GetMapping({ "/getOrderDetails" })
	public List<OrderDetailProxy> getOrderDetails() {
		return orderDetailService.getOrderDetails();
	}

	@GetMapping({ "/getAllOrderDetails" })
	public List<OrderDetailProxy> getAllOrderDetails() {
		return orderDetailService.getAllOrderDetails();
	}

	@PreAuthorize("hasRole('User')")
	@GetMapping({ "/getorderdetails/{username}" })
	public Page<OrderDetailProxy> getProducts(@PathVariable("username") String username,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "orderId") String sortBy, @RequestParam(defaultValue = "asc") String sortDir) {
		System.err.println("orderdetails controller" + username);
		Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "orderDate");
		PageRequest pageable = PageRequest.of(page, size, sort);
		return orderDetailService.getAllorderPageWise(username, pageable);
	}

	@PreAuthorize("hasRole('Seller')")
	@GetMapping("/seller/{sellerName}")
	public List<OrderDetailProxy> getOrdersBySeller(@PathVariable String sellerName) {
		System.out.println("getOrdersBySeller");
		return orderDetailService.getOrdersBySeller(sellerName);
	}
	
}
