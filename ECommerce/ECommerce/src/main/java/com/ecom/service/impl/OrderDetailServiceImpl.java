package com.ecom.service.impl;

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
import com.ecom.dao.PaymentDao;
import com.ecom.dao.ProductDao;
import com.ecom.dao.UserDao;
import com.ecom.entity.Cart;
//import com.ecom.entity.Cart;
import com.ecom.entity.OrderDetail;
import com.ecom.entity.OrderInput;
import com.ecom.entity.OrderPaymentInput;
import com.ecom.entity.OrderProductQuantity;
import com.ecom.entity.PaymentDetail;
//import com.ecom.entity.PaymentDetail;
import com.ecom.entity.PaymentInput;
import com.ecom.entity.Product;
import com.ecom.entity.User;
import com.ecom.proxy.OrderDetailProxy;
import com.ecom.service.EmailService;
import com.ecom.service.OrderDetailService;
import com.ecom.util.MapperUtil;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

	private static final String ORDER_PLACED = "Placed";

	@Autowired
	private OrderDetailDao orderDetailDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CartDao cartDao;

	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private MapperUtil mappper;
	
	@Autowired
	private EmailService emailService;

	public List<OrderDetailProxy> getAllOrderDetails() {
		List<OrderDetail> orderDetails = new ArrayList<>();
		orderDetailDao.findAll().forEach(e -> orderDetails.add(e));

		return mappper.convertList(orderDetails, OrderDetailProxy.class);
	}

	public List<OrderDetailProxy> getOrderDetails() {
		String currentUser = JwtRequestFilter.CURRENT_USER;
		User user = userDao.findById(currentUser).get();
		return mappper.convertList(orderDetailDao.findByUser(user), OrderDetailProxy.class);
	}

	public void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout) {
		System.out.println("place order service");
		List<OrderProductQuantity> productQuantityList = orderInput.getOrderProductQuantityList();

		for (OrderProductQuantity o : productQuantityList) {
			Product product = productDao.findById((long) o.getProductId()).get();

			String currentUser = JwtRequestFilter.CURRENT_USER;
			User user = userDao.findById(currentUser).get();

			OrderDetail orderDetail = new OrderDetail(orderInput.getFullName(), orderInput.getFullAddress(),
					orderInput.getContactNumber(), orderInput.getAlternateContactNumber(), ORDER_PLACED,
					product.getProductDiscountedPrice() * o.getQuantity(), product, user);

			if (!isSingleProductCheckout) {

				List<Cart> carts = cartDao.findByUser(user);
				carts.stream().forEach(x -> cartDao.deleteById(x.getCartId()));

			}
			orderDetailDao.save(orderDetail);

		}

	}

	@Override
    public void placeOrderWithPayment(OrderPaymentInput orderPaymentInput, boolean isSingleProductCheckout) {
        System.out.println("place order with payment service");

        OrderInput orderInput = orderPaymentInput.getOrderDetails();
        PaymentInput paymentDetails = orderPaymentInput.getPaymentDetails();

        // Process the order and get the order details
        List<OrderDetail> processedOrders = processOrder(orderInput, isSingleProductCheckout, paymentDetails);
        
        // For each order, generate and send a PDF bill
        for (OrderDetail orderDetail : processedOrders) {
            try {
                // Generate the PDF bill
                byte[] pdfBytes = OrderBillPDFGenerator.generateOrderBillPDF(orderDetail);
                
                // Send email with the PDF attachment
                emailService.sendOrderConfirmationEmail(orderDetail, pdfBytes);
                
            } catch (Exception e) {
                // Log the error but don't fail the order process
                System.err.println("Failed to generate or send order bill: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // This method processes the order and returns the list of created OrderDetail objects
    private List<OrderDetail> processOrder(OrderInput orderInput, boolean isSingleProductCheckout, PaymentInput paymentDetails) {
        List<OrderProductQuantity> productQuantityList = orderInput.getOrderProductQuantityList();
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderProductQuantity o : productQuantityList) {
            Product product = productDao.findById((long) o.getProductId()).get();

            String currentUser = JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(currentUser).get();

            OrderDetail orderDetail = new OrderDetail(
                orderInput.getFullName(),
                orderInput.getFullAddress(),
                orderInput.getContactNumber(),
                orderInput.getAlternateContactNumber(),
                ORDER_PLACED,
                product.getProductDiscountedPrice() * o.getQuantity(),
                product,
                user
            );

            

            orderDetailDao.save(orderDetail);
            orderDetails.add(orderDetail);
        }

        // Clear cart if this is not a single product checkout
        if (!isSingleProductCheckout) {
            String currentUser = JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(currentUser).get();
            List<Cart> carts = cartDao.findByUser(user);
            carts.forEach(cart -> cartDao.deleteById(cart.getCartId()));
        }

        return orderDetails;
    }

 
    public byte[] generateOrderBill(Long orderId) {
        try {
            OrderDetail orderDetail = orderDetailDao.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
            
            return OrderBillPDFGenerator.generateOrderBillPDF(orderDetail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate order bill PDF: " + e.getMessage());
        }
    }

	public Page<OrderDetailProxy> getAllorderPageWise(String username, Pageable pageable) {
		User user = userDao.findByUserName(username).get();
		Page<OrderDetail> products = orderDetailDao.findByUser(user, pageable);
		System.err.println("Fetching products for user name: " + username);

		List<OrderDetail> updatedOrderDetails = products.stream().map(orderDetail -> {
			if (orderDetail.getProduct() != null) {
				orderDetail.getProduct().setProductImages(null);
			}
			return orderDetail;
		}).collect(Collectors.toList());
		return new PageImpl<>(mappper.convertList(updatedOrderDetails, OrderDetailProxy.class), pageable,
				products.getTotalElements());
	}

	@Transactional
	public void deleteOrderDetailsByProductId(Long productId) {
		orderDetailDao.deleteByProduct_ProductId(productId);
	}

	@Transactional
	public void deleteProductAndRelatedOrders(Long productId) {
		deleteOrderDetailsByProductId(productId);
		productDao.deleteById(productId);
	}

	public List<OrderDetailProxy> getOrdersBySeller(String sellerName) {
		return mappper.convertList(orderDetailDao.findByProduct_Sellername(sellerName), OrderDetailProxy.class);

	}
	
	// Add this method to your OrderService class
	
	

}
