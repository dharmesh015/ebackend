package com.ecom.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecom.entity.OrderDetail;
import com.ecom.entity.OrderInput;
import com.ecom.entity.OrderPaymentInput;
import com.ecom.proxy.OrderDetailProxy;

public interface OrderDetailService {
	List<OrderDetailProxy> getAllOrderDetails();

	List<OrderDetailProxy> getOrderDetails();

	void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout);

	Page<OrderDetailProxy> getAllorderPageWise(String username, Pageable pageable);

	void deleteOrderDetailsByProductId(Long productId);

	void deleteProductAndRelatedOrders(Long productId);

	void placeOrderWithPayment(OrderPaymentInput orderPaymentInput, boolean isSingleProductCheckout);

	List<OrderDetailProxy> getOrdersBySeller(String sellerName);


}
