package com.ecom.dao;

import org.springframework.data.repository.CrudRepository;

import com.ecom.entity.OrderDetail;
import com.ecom.entity.PaymentDetail;

public interface PaymentDao extends CrudRepository<PaymentDetail, Long> {
    PaymentDetail findByRazorpayPaymentId(String razorpayPaymentId);
    void deleteByOrder(OrderDetail order);
    
}
