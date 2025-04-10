package com.ecom.proxy;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailProxy {

    private Long orderId;

    @NotNull(message = "Full name cannot be null")
    private String orderFullName;

    @NotNull(message = "Order cannot be null")
    private String orderFullOrder;

    @NotNull(message = "Contact number cannot be null")
    private String orderContactNumber;

    private String orderAlternateContactNumber;

    private String orderStatus;

    @NotNull(message = "Order amount cannot be null")
    private Double orderAmount;

    private ProductProxy product; // Assuming you want to send only the product ID

    private UserProxy user; 
    
    private LocalDateTime orderDate; // Assuming you want to send only the user name

}
