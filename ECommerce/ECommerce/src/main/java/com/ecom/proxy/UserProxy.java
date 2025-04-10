package com.ecom.proxy;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserProxy {
	
	    @NotNull(message = "Username cannot be null")
	    private String userName;

	    private String userFirstName;
	    private String userLastName;
	    @NotNull(message = "Email cannot be null")
	    private String email;

	    private String mobileNumber;
	    private String userPassword;
	    private String address;

	    // Assuming you want to send only the role IDs instead of the entire Role objects
	    private Set<RoleProxy> role; // List of role IDs
}
