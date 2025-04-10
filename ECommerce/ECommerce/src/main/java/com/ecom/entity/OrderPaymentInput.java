package com.ecom.entity;


public class OrderPaymentInput {
    
    private OrderInput orderDetails;
    private PaymentInput paymentDetails;
    
    public OrderPaymentInput() {
    }
    
    public OrderPaymentInput(OrderInput orderDetails, PaymentInput paymentDetails) {
        this.orderDetails = orderDetails;
        this.paymentDetails = paymentDetails;
    }

    public OrderInput getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderInput orderDetails) {
        this.orderDetails = orderDetails;
    }

    public PaymentInput getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentInput paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}