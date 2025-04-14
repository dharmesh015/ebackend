package com.ecom.service.impl;

import java.util.List;
import java.util.Optional;
import com.ecom.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.dao.CartDao;
import com.ecom.dao.OrderDetailDao;
import com.ecom.dao.PaymentDao;
import com.ecom.dao.ProductDao;
import com.ecom.dao.RoleDao;
import com.ecom.dao.UserDao;
import com.ecom.dao.UserImageDao;
import com.ecom.entity.OrderDetail;
import com.ecom.entity.Product;
import com.ecom.entity.Role;
import com.ecom.entity.User;
import com.ecom.proxy.UserProxy;
import com.ecom.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{

    private final MapperUtil mapperUtil;

	
	
	@Autowired
	private UserDao userDao;
	
	
	@Autowired
	private RoleDao roleDao;
	
	
	@Autowired
	private UserImageDao imageDao;
	
	@Autowired
	private CartDao cartdao;
	
	@Autowired
	private OrderDetailDao orddrdao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
  private ProductDao productDao;


    AdminServiceImpl(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }
	
	



	public Page<UserProxy> getAllUsersPageWise(PageRequest pageable) {
		System.err.println("pagewise service");
//    	System.err.println(userDao.findAll(pageable));
    	 return userDao.findAll(pageable); 
     
	}


//	@Transactional
//	public void deleteUser (String userName) {
//	    User user = userDao.findById(userName).orElseThrow(() -> new RuntimeException("User  not found"));
//
//	    // Delete payment details associated with the user's orders
//	    List<OrderDetail> orders = orddrdao.findByUser (user);
//	    for (OrderDetail order : orders) {
//	        paymentDao.deleteByOrder(order); // Assuming you have a method to delete payment details by order
//	        orddrdao.delete(order); // Delete the order
//	    }
//
//	    // Check if the user has the "Seller" role
//	    Optional<Role> sellerRole = roleDao.findByRoleName("Seller");
//	    System.err.println(user.getRole().contains(sellerRole.get()));
//	    if (sellerRole.isPresent() && user.getRole().contains(sellerRole.get())) {
//	        List<Product> products = productDao.findBySellername(user.getUserName());
//	        for (Product product : products) {
//	            productDao.deleteById(product.getProductId()); // Delete the product by ID
//	        }
//	    }
//
//	    // Delete user's cart and images
//	    cartdao.deleteByUser (user);
//	    imageDao.deleteByUser (user);
//	    
//	    // Clear user roles and delete the user
//	    user.getRole().clear();
//	    userDao.delete(user);
//	}
	@Transactional
	public void deleteUser(String userName) {
	    User user = userDao.findById(userName).orElseThrow(() -> new RuntimeException("User not found"));
	    
	    // Delete payment details associated with the user's orders
	    List<OrderDetail> orders = orddrdao.findByUser(user);
	    for (OrderDetail order : orders) {
	        paymentDao.deleteByOrder(order); // Assuming you have a method to delete payment details by order
	        orddrdao.delete(order); // Delete the order
	    }
	    
	    // Check if the user has the "Seller" role
	    Optional<Role> sellerRole = roleDao.findByRoleName("Seller");
	    if (sellerRole.isPresent() && user.getRole().contains(sellerRole.get())) {
	        System.err.println("User " + userName + " is a seller. Deleting their products.");
	        
	        // Find all products by this seller
	        List<Product> products = productDao.findBySellername(user.getUserName());
	        
	        for (Product product : products) {
	            // First, delete carts that reference this product
	            cartdao.deleteByProduct(product);
	            
	            // Then delete orders that reference this product
	            // Note: We've already deleted user's own orders above, this handles other users' orders for this seller's products
	            List<OrderDetail> productOrders = orddrdao.findByProduct(product);
	            for (OrderDetail order : productOrders) {
	                paymentDao.deleteByOrder(order);
	                orddrdao.delete(order);
	            }
	            
	            // Now we can safely delete the product
	            productDao.deleteById(product.getProductId());
	        }
	    }
	    
	    // Delete user's cart
	    cartdao.deleteByUser(user);
	    
	    // Delete user's images
	    imageDao.deleteByUser(user);
	    
	    // Clear user roles and delete the user
	    user.getRole().clear();
	    userDao.delete(user);
	    
	    System.err.println("User " + userName + " successfully deleted");
	}
	
	public String updateUser(UserProxy user) {
		 User userobj = userDao.findById(user.getUserName()).orElseThrow(() -> new RuntimeException("User  not found"));
		 userobj.setUserPassword(userobj.getUserPassword()); 
		 userobj.setRole(userobj.getRole());
		 userobj.setEmail(user.getEmail());
		 userobj.setAddress(user.getAddress());
		 userobj.setMobileNumber(user.getMobileNumber());
		 userobj.setUserFirstName(user.getUserFirstName());
		 userobj.setUserLastName(user.getUserLastName());
	
	        
	       userDao.save(userobj);
	       return "saved";
	}


	public UserProxy getuser(String name) {
		Optional<User> userdata = userDao.findById(name);
		return  mapperUtil.convertValue( userdata.get(),UserProxy.class);
	}
}
