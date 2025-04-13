package com.ecom.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.ecom.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.configuration.JwtRequestFilter;
import com.ecom.dao.CartDao;
import com.ecom.dao.OrderDetailDao;
import com.ecom.dao.ProductDao;
import com.ecom.dao.UserDao;
import com.ecom.entity.Cart;
import com.ecom.entity.OrderDetail;
import com.ecom.entity.Product;
import com.ecom.entity.User;
import com.ecom.proxy.CartProxy;
import com.ecom.proxy.OrderDetailProxy;
import com.ecom.proxy.ProductProxy;
import com.ecom.proxy.UserProxy;
import com.ecom.service.OrderDetailService;
import com.ecom.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

//	private final MapperUtil mapperUtil;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private UserDao userdao;

	@Autowired
	private CartDao cartdao;

	@Autowired
	private OrderDetailDao orderdetaildao;
	
	@Autowired
	private OrderDetailService orderDetailService;

	@Autowired
	private MapperUtil mapperUtil;

	@Transactional
	public ProductProxy addNewProduct(ProductProxy productproxy) {
		Product convertproduct = mapperUtil.convertValue(productproxy, Product.class);
		return mapperUtil.convertValue(productDao.save(convertproduct), ProductProxy.class);
	}

	public List<ProductProxy> getAllProducts() {

		return  mapperUtil.convertList((List<Product>) productDao.findAll(), ProductProxy.class);

	}

	@Transactional
	public void deleteProductById(Long productId) {

		orderDetailService.deleteOrderDetailsByProductId(productId); // Delete related orders first
		productDao.deleteById(productId);
	}

	public ProductProxy updateProduct(ProductProxy productproxy) {

		if (productDao.existsById(productproxy.getProductId())) {
			Product saveproduct = productDao.save(mapperUtil.convertValue(productproxy, Product.class));
			return mapperUtil.convertValue(saveproduct, ProductProxy.class);
		} else {
			throw new RuntimeException("Product not found with id: " + productproxy.getProductId());
		}
	}

	public ProductProxy getProductById(Long productId) {
		Optional<Product> product = productDao.findById(productId);
		if (product.isEmpty()) {
			return null;
		}
		return mapperUtil.convertValue(product.get(), ProductProxy.class);
	}

	public List<ProductProxy>getProductDetails(boolean isSingeProductCheckout, Integer productId) {

		if (isSingeProductCheckout && productId != 0) {
			List<Product> list = new ArrayList<>();
			Long id = (long) productId;
			Product product = productDao.findById(id).get();
			list.add(product);
			
			System.out.println(list.getFirst().getProductDescription());
			 List<ProductProxy> convertList = mapperUtil.convertList(list, ProductProxy.class);
			 return convertList;
		} else {

//			String username = JwtRequestFilter.CURRENT_USER;
//			User user = userDao.findById(username).get();
//			List<Cart>  carts= cartDao.findByUser(user);
//			
//			return carts.stream().map(x -> x.getProduct()).collect(Collectors.toList());

		}

		return new ArrayList<>();

	}

	public Page<ProductProxy> getAllProductsPageWise(Pageable pageable) {
		System.err.println("pagewise service");
		System.err.println(productDao.findAll(pageable));
		
		return productDao.findAll(pageable);
 

	}

	public Page<ProductProxy> getAllProductsPageWiseByUser(Pageable pageable) {
		System.err.println("pagewise service");
		System.err.println(productDao.findAll(pageable));
		
		Page<Product> products = productDao.findBySellername(getCurrentUsername(), pageable);
		
		  List<Product> updatedOrderDetails = products.stream()
		            .collect(Collectors.toList());
	              
		    return new PageImpl<>(mapperUtil.convertList(updatedOrderDetails, ProductProxy.class), pageable, products.getTotalElements());
//		return ;
	}

	public Page<ProductProxy>getproductbyusername(String username, Pageable pageable) {
		User user = userdao.findByUserName(username).get();
		Page<Product> products = productDao.findBySellername(username, pageable);
		System.err.println("Fetching products for user name: " + username);
		List<Product> updatedOrderDetails = products.stream()
	            .collect(Collectors.toList());
		  return new PageImpl<>(mapperUtil.convertList(updatedOrderDetails, ProductProxy.class), pageable, products.getTotalElements());

	}

	public List<CartProxy> getCartDetails() {
		String username = getCurrentUsername();
		User user = userdao.findById(username).orElse(null);
		if (user != null) {
			
			return mapperUtil.convertList(cartdao.findByUser(user), CartProxy.class);
		}
		return null;
	}

	private String getCurrentUsername() {

		return JwtRequestFilter.CURRENT_USER;
	}

}