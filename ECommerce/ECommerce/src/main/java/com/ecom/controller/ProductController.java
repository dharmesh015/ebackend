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
    @PostMapping(value = {"/addNewProduct"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Product addNewProduct(@RequestPart("product") Product product,
                                 @RequestPart("imageFile") MultipartFile[] file) {        
//        System.out.println("controller id--"+product.getProductId());
		product.setProductId(null);
    	
        try {
            Set<ImageModel> images = uploadImage(file);
            product.setProductImages(images);
            return productService.addNewProduct(product); 
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return null;
        }
    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();
        for (MultipartFile file : multipartFiles) {
            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes());
            imageModels.add(imageModel);
        }
        return imageModels;
    }
    
    @GetMapping({"/getAllProducts"})
	public List<Product> getAllProducts(){
    	System.out.println("controller");
		return productService.getAllProducts();
	}
    
    @DeleteMapping("/deleteProduct/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
    	System.err.println("delete conroller");
        productService.deleteProductById(productId);
    }
    
	@PreAuthorize("hasRole('Seller')")
    @PostMapping(value = {"/updateProduct"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Product updateProduct(@RequestPart("product") Product product,
                                 @RequestPart(value = "imageFile", required = false) MultipartFile[] file) {
       System.out.println("product update controller");
    	try {
            // If new images are provided, upload them
            if (file != null && file.length > 0) {
                Set<ImageModel> images = uploadImage(file);
                product.setProductImages(images);
            }
            return productService.updateProduct(product);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return null;
        }
    }
	
	///ahi change hee
//	 @GetMapping("/getProductById/{productId}")
//	    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
//	        Product product = productService.getProductById(productId);
//	        if (product != null) {
//	            return ResponseEntity.ok(product);
//	        } else {
//	            return ResponseEntity.notFound().build();
//	        }
//	    }
//	 
	 @GetMapping({"/getProductById/{productId}"})
	    public Product getProductById(@PathVariable("productId") Long productId) {
	        return productService.getProductById(productId);
	    }
	 
	 //ahe change hee
//	 @PreAuthorize("hasRole('User')")
//		@GetMapping({"/getProductDetails/{isSingeProductCheckout}/{productId}"})
//		public List<Product> getProductDetails(@PathVariable(name="isSingeProductCheckout") boolean isSingeProductCheckout,
//											@PathVariable(name= "productId") Integer productId) {
//			
//			return productService.getProductDetails(isSingeProductCheckout, productId);
//			
//			
//		}
	 @PreAuthorize("hasRole('User')")
	    @GetMapping({"/getProductDetails/{isSingleProductCheckout}/{productId}"})
	    public List<Product> getProductDetails(
	            @PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout,
	            @PathVariable(name = "productId") Long productId) {
	        
	        List<Product> products = new ArrayList<>();
	        
	        if (isSingleProductCheckout && productId != 0) {
	            // Single product checkout
	            Product product = productService.getProductById(productId);
	            products.add(product);
	        } else {
	            // Cart checkout - get all products from user's cart
	            // Assuming you have a method to get the current user's cart items
	            List<Cart> carts = productService.getCartDetails();
	            
	            if (carts != null) {
	                for (Cart cart : carts) {
	                    products.add(cart.getProduct());
	                }
	            }
	        }
	        
	        return products;
	    }
		
		@GetMapping("/getAllProductsPageWise")
	    public Page<Product> getAllProducts(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestParam(defaultValue = "id") String sortBy,
	            @RequestParam(defaultValue = "asc") String sortDir) {
	        System.err.println("pagewise controller");
	        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
	        PageRequest pageable = PageRequest.of(page, size, sort);
	        return productService.getAllProductsPageWise(pageable);
	    }
		
		@GetMapping({"/getallproduct/{username}"})
	    public Page<Product> getProducts(
	    		@PathVariable("username") String username,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestParam(defaultValue = "orderId") String sortBy,
	            @RequestParam(defaultValue = "asc") String sortDir)
		{
	        System.err.println("orderdetails controller"+username);
	        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "orderDate");
	        PageRequest pageable = PageRequest.of(page, size, sort);
	        return productService.getproductbyusername(username,pageable);
	    }
}