package com.ecom.proxy;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ProductProxy {

    private Long productId;

    @NotNull(message = "Product name cannot be null")
    private String productName;

    private String productDescription;

    private Double productDiscountedPrice;

    private Double productActualPrice;

    private String sellername;

    // Assuming you want to send only the image IDs instead of the entire ImageModel objects
    private Set<ImageModelProxy> productImages; // List of image IDs

}