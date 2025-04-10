package com.ecom.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.ecom.entity.Cart;
import com.ecom.entity.ImageModel;
import com.ecom.entity.OrderDetail;
import com.ecom.entity.Product;
import com.ecom.proxy.CartProxy;
import com.ecom.proxy.ImageModelProxy;
import com.ecom.proxy.ProductProxy;
import com.ecom.service.ProductService;

@RestController
@CrossOrigin(value = "http://localhost:4200")
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
	public ResponseEntity<Void> handleOptions() {
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('Seller')")
	@PostMapping(value = { "/addNewProduct" }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ProductProxy addNewProduct(@RequestPart("product") ProductProxy product,
			@RequestPart("imageFile") MultipartFile[] file) {

		product.setProductId(null);

		try {
			Set<ImageModelProxy> images = uploadImage(file);
			product.setProductImages(images);
			return productService.addNewProduct(product);
		} catch (Exception e) {
			System.out.println("Error occurred: " + e.getMessage());
			return null;
		}
	}

	public Set<ImageModelProxy> uploadImage(MultipartFile[] multipartFiles) throws IOException {
		Set<ImageModelProxy> imageModels = new HashSet<>();
		for (MultipartFile file : multipartFiles) {
			ImageModelProxy imageModel = new ImageModelProxy( file.getOriginalFilename(), file.getContentType(), file.getBytes());
			imageModels.add(imageModel);
		}
		return imageModels;
	}

	@GetMapping({ "/getAllProducts" })
	public List<ProductProxy> getAllProducts() {
		return productService.getAllProducts();
	}

	@DeleteMapping("/deleteProduct/{productId}")
	public void deleteProduct(@PathVariable Long productId) {
		System.err.println("delete conroller");
		productService.deleteProductById(productId);
	}

	@PreAuthorize("hasRole('Seller')")
	@PostMapping(value = { "/updateProduct" }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ProductProxy updateProduct(@RequestPart("product") ProductProxy product,
			@RequestPart(value = "imageFile", required = false) MultipartFile[] file) {
		try {
			// If new images are provided, upload them
			if (file != null && file.length > 0) {
				Set<ImageModelProxy> images = uploadImage(file);
				product.setProductImages(images);
			}
			return productService.updateProduct(product);
		} catch (Exception e) {
			System.out.println("Error occurred: " + e.getMessage());
			return null;
		}
	}

	@GetMapping({ "/getProductById/{productId}" })
	public ProductProxy getProductById(@PathVariable("productId") Long productId) {
		return productService.getProductById(productId);
	}

	@PreAuthorize("hasRole('User')")
	@GetMapping({ "/getProductDetails/{isSingleProductCheckout}/{productId}" })
	public List<ProductProxy> getProductDetails(
			@PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout,
			@PathVariable(name = "productId") Long productId) {

		List<ProductProxy> products = new ArrayList<>();

		if (isSingleProductCheckout && productId != 0) {
			// Single product checkout
			ProductProxy product = productService.getProductById(productId);
			products.add(product);
		} else {
			// Cart checkout - get all products from user's cart
			// Assuming you have a method to get the current user's cart items
			List<CartProxy> carts = productService.getCartDetails();

			if (carts != null) {
				for (CartProxy cart : carts) {
					products.add(cart.getProduct());
				}
			}
		}

		return products;
	}

	@GetMapping("/getAllProductsPageWise")
	public Page<ProductProxy> getAllProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		System.err.println("pagewise controller");
		Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		PageRequest pageable = PageRequest.of(page, size, sort);
		return productService.getAllProductsPageWise(pageable);
	}
	
	@PreAuthorize("hasRole('Seller')")
	@GetMapping("/getAllProductsPageWiseByUser")
	public Page<ProductProxy> getAllProduct(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		System.err.println("pagewise controller");
		Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		PageRequest pageable = PageRequest.of(page, size, sort);
		return productService.getAllProductsPageWiseByUser(pageable);
	}
	
	
}