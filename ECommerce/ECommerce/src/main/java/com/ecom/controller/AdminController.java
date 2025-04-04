package com.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.entity.Product;
import com.ecom.entity.User;
import com.ecom.service.AdminService;

@RestController
@CrossOrigin
public class AdminController {

	@Autowired
	private AdminService adminservice;

//	@PreAuthorize("hasRole('Admin')")
//	@GetMapping({"/admin/getAlluser"})
//	public List<User> getalluser() {
//		
//		return adminservice.getAllUser();	
//	}
	
	
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/getAllUsersPageWise")
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        System.err.println("pagewise controller");
//        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        PageRequest pageable = PageRequest.of(page, size);
        return adminservice.getAllProductsPageWise(pageable);
    }
	

	    @PreAuthorize("hasRole('Admin')")
		@DeleteMapping("/deleteUser/{userName}")
	    public ResponseEntity<Void> deleteUserByUserName(@PathVariable ("userName")String userName) {
			System.err.println("delete controller--"+userName);
	    	adminservice.deleteUser(userName);
	        return ResponseEntity.noContent().build(); // Return 204 No Content
	    }
}
