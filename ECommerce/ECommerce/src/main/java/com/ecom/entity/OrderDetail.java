//package com.ecom.entity;
//
//import jakarta.persistence.*;
//import java.time.LocalDate;
//
//@Entity
//public class OrderDetail {
//    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long orderId;
//    private String orderFullName;
//    private String orderFullOrder;
//    private String orderContactNumber;
//    private String orderAlternateContactNumber;
//    private String orderStatus;
//    private Double orderAmount;
//
//    @ManyToOne
//    @JoinColumn(name = "product_product_id")
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "user_user_name")
//    private User user;
//
//    @Column(name = "order_date")
//    private LocalDate orderDate; // New field to store the order date
//
//    public OrderDetail() {
//        super();
//    }
//
//    public OrderDetail(String orderFullName, String orderFullOrder, String orderContactNumber,
//                       String orderAlternateContactNumber, String orderStatus, Double orderAmount, 
//                       Product product, User user) {
//        super();
//        this.orderFullName = orderFullName;
//        this.orderFullOrder = orderFullOrder;
//        this.orderContactNumber = orderContactNumber;
//        this.orderAlternateContactNumber = orderAlternateContactNumber;
//        this.orderStatus = orderStatus;
//        this.orderAmount = orderAmount;
//        this.product = product;
//        this.user = user;
//    }
//
//    // PrePersist method to set the order date before persisting
//    @PrePersist
//    protected void onCreate() {
//        this.orderDate = LocalDate.now(); // Set the current date
//    }
//
//    // Getters and Setters
//    public Long getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(Long orderId) {
//        this.orderId = orderId;
//    }
//
//    public String getOrderFullName() {
//        return orderFullName;
//    }
//
//    public void setOrderFullName(String orderFullName) {
//        this.orderFullName = orderFullName;
//    }
//
//    public String getOrderFullOrder() {
//        return orderFullOrder;
//    }
//
//    public void setOrderFullOrder(String orderFullOrder) {
//        this.orderFullOrder = orderFullOrder;
//    }
//
//    public String getOrderContactNumber() {
//        return orderContactNumber;
//    }
//
//    public void setOrderContactNumber(String orderContactNumber) {
//        this.orderContactNumber = orderContactNumber;
//    }
//
//    public String getOrderAlternateContactNumber() {
//        return orderAlternateContactNumber;
//    }
//
//    public void setOrderAlternateContactNumber(String orderAlternateContactNumber) {
//        this.orderAlternateContactNumber = orderAlternateContactNumber;
//    }
//
//    public String getOrderStatus() {
//        return orderStatus;
//    }
//
//    public void setOrderStatus(String orderStatus) {
//        this.orderStatus = orderStatus;
//    }
//
//    public Double getOrderAmount() {
//        return orderAmount;
//    }
//
//    public void setOrderAmount(Double orderAmount) {
//        this.orderAmount = orderAmount;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
//
//    public User getUser () {
//        return user;
//    }
//
//    public void setUser (User user) {
//        this.user = user;
//    }
//
//    public LocalDate getOrderDate() {
//        return orderDate;
//    }
//
//    public void setOrderDate(LocalDate orderDate) {
//        this.orderDate = orderDate;
//    }
//}

package com.ecom.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class OrderDetail {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String orderFullName;
    private String orderFullOrder;
    private String orderContactNumber;
    private String orderAlternateContactNumber;
    private String orderStatus;
    private Double orderAmount;

    @ManyToOne
    @JoinColumn(name = "product_product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_user_name")
    private User user;

    @Column(name = "order_date")
    private LocalDate orderDate; // New field to store the order date

    public OrderDetail() {
        super();
    }

    public OrderDetail(String orderFullName, String orderFullOrder, String orderContactNumber,
                       String orderAlternateContactNumber, String orderStatus, Double orderAmount, 
                       Product product, User user) {
        super();
        this.orderFullName = orderFullName;
        this.orderFullOrder = orderFullOrder;
        this.orderContactNumber = orderContactNumber;
        this.orderAlternateContactNumber = orderAlternateContactNumber;
        this.orderStatus = orderStatus;
        this.orderAmount = orderAmount;
        this.product = product;
        this.user = user;
    }

    // PrePersist method to set the order date before persisting
    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDate.now(); // Set the current date
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderFullName() {
        return orderFullName;
    }

    public void setOrderFullName(String orderFullName) {
        this.orderFullName = orderFullName;
    }

    public String getOrderFullOrder() {
        return orderFullOrder;
    }

    public void setOrderFullOrder(String orderFullOrder) {
        this.orderFullOrder = orderFullOrder;
    }

    public String getOrderContactNumber() {
        return orderContactNumber;
    }

    public void setOrderContactNumber(String orderContactNumber) {
        this.orderContactNumber = orderContactNumber;
    }

    public String getOrderAlternateContactNumber() {
        return orderAlternateContactNumber;
    }

    public void setOrderAlternateContactNumber(String orderAlternateContactNumber) {
        this.orderAlternateContactNumber = orderAlternateContactNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser () {
        return user;
    }

    public void setUser (User user) {
        this.user = user;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}