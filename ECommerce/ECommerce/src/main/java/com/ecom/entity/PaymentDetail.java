package com.ecom.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;



@Entity
@Table(name = "payment_details")
public class PaymentDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    
    @Column(nullable = false)
    private String razorpayPaymentId;
    
    private String razorpayOrderId;
    
    private String razorpaySignature;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private Date paymentDate;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "orderId")
    private OrderDetail order;
    
    public PaymentDetail() {
        this.paymentDate = new Date();
    }
    
    public PaymentDetail(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature, 
                        Double amount, String status, OrderDetail order) {
        this.razorpayPaymentId = razorpayPaymentId;
        this.razorpayOrderId = razorpayOrderId;
        this.razorpaySignature = razorpaySignature;
        this.amount = amount;
        this.status = status;
        this.order = order;
        this.paymentDate = new Date();
    }

    
}
