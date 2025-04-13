
package com.ecom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDateTime orderDate; 

  

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

//     PrePersist method to set the order date before persisting
    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now(); // Set the current date
    }

    
}