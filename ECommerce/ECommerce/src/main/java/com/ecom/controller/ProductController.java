package com.ecom.controller;

import java.io.IOException;
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

import com.ecom.entity.ImageModel;
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

	@PreAuthorize("hasRole('Admin')")
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
    
	@PreAuthorize("hasRole('Admin')")
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
	
	 @GetMapping("/getProductById/{productId}")
	    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
	        Product product = productService.getProductById(productId);
	        if (product != null) {
	            return ResponseEntity.ok(product);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 
//	 @PreAuthorize("hasRole('User')")
		@GetMapping({"/getProductDetails/{isSingeProductCheckout}/{productId}"})
		public List<Product> getProductDetails(@PathVariable(name="isSingeProductCheckout") boolean isSingeProductCheckout,
											@PathVariable(name= "productId") Integer productId) {
			
			return productService.getProductDetails(isSingeProductCheckout, productId);
			
			
		}
}