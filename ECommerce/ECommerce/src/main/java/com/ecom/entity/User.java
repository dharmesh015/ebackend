
package com.ecom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {


	@Id
	private String userName;
	private String userFirstName;
	private String userLastName;
	private String email;
	private String mobileNumber;
	private String userPassword;
	private String address;

	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL, CascadeType.REMOVE})
	@JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	private Set<Role> role;
	

}
