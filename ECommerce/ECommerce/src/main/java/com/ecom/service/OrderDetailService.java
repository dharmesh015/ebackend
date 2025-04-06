package com.ecom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.configuration.JwtRequestFilter;
import com.ecom.dao.CartDao;
//import com.ecom.dao.CartDao;
import com.ecom.dao.OrderDetailDao;
import com.ecom.dao.ProductDao;
import com.ecom.dao.UserDao;
import com.ecom.entity.Cart;
//import com.ecom.entity.Cart;
import com.ecom.entity.OrderDetail;
import com.ecom.entity.OrderInput;
import com.ecom.entity.OrderProductQuantity;
import com.ecom.entity.Product;
import com.ecom.entity.User;

@Service
public class OrderDetailService {
	
	private static final String ORDER_PLACED = "Placed";  
	
	@Autowired
	private OrderDetailDao orderDetailDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CartDao cartDao;
	
	public List<OrderDetail> getAllOrderDetails(){
		List<OrderDetail> orderDetails = new ArrayList<>();
		orderDetailDao.findAll().forEach(e -> orderDetails.add(e));
		
		return orderDetails;
	}
	
	public List<OrderDetail> getOrderDetails() {
		String currentUser = JwtRequestFilter.CURRENT_USER;
		User user = userDao.findById(currentUser).get();
		
		return orderDetailDao.findByUser(user);
	}
	
	public void placeOrder(OrderInput orderInput,boolean isSingleProductCheckout) {
		System.out.println("place order service");
	List<OrderProductQuantity> productQuantityList = orderInput.getOrderProductQuantityList();
	
	for(OrderProductQuantity o: productQuantityList) {
		Product product = productDao.findById((long)o.getProductId()).get();
			
		String currentUser = JwtRequestFilter.CURRENT_USER;
		User user= userDao.findById(currentUser).get();
			
			OrderDetail orderDetail = new OrderDetail(
				orderInput.getFullName(),
				orderInput.getFullAddress(),
				orderInput.getContactNumber(),
				orderInput.getAlternateContactNumber(),
					ORDER_PLACED,
					product.getProductDiscountedPrice()*o.getQuantity(),
					product,
				user);
		
		if(!isSingleProductCheckout) {
				
				List<Cart> carts= cartDao.findByUser(user);
			carts.stream().forEach(x -> cartDao.deleteById(x.getCartId()));			
				
			}
			orderDetailDao.save(orderDetail);
//		}
	}
	
	}
	
	public Page<OrderDetail> getAllorderPageWise(String username, Pageable pageable) {
	    User user = userDao.findByUserName(username).get();
	    Page<OrderDetail> products = orderDetailDao.findByUser(user, pageable);
	    System.err.println("Fetching products for user name: " + username);

	    List<OrderDetail> updatedOrderDetails = products.stream()
	            .map(orderDetail -> {
	                if (orderDetail.getProduct() != null) {
	                    orderDetail.getProduct().setProductImages(null); // Set product images to null
	                }
	                return orderDetail; // Return the modified order detail
	            })
	            .collect(Collectors.toList());
	    System.out.print( updatedOrderDetails.getFirst().getOrderDate());
	    // Return a new Page with the updated order details
	    return new PageImpl<>(updatedOrderDetails, pageable, products.getTotalElements());
	}
	
	 @Transactional // Add Transactional annotation
	    public void deleteOrderDetailsByProductId(Long productId) {
	        orderDetailDao.deleteByProduct_ProductId(productId);
	    }

	    @Transactional
	    public void deleteProductAndRelatedOrders(Long productId) {
	        deleteOrderDetailsByProductId(productId);
	        productDao.deleteById(productId);
	    }

}
