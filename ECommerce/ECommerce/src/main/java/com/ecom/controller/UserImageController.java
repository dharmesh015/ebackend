package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.service.impl.UserImageService;

import java.io.IOException;

@RestController
//@RequestMapping("/api/user")
public class UserImageController {

    @Autowired
    private UserImageService userImageService;
    
    @PostMapping("/upload-image")
//    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> uploadImage(
            @RequestParam("userName") String userName,
            @RequestParam("imageFile") MultipartFile file) {
    	System.out.println("upload image");
        try {
            userImageService.uploadImage(userName, file);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }
    
    @GetMapping(value = "/image/{userName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getUserImage(@PathVariable String userName) {
        byte[] imageData = userImageService.getImageByUserName(userName);
        
        if (imageData == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(imageData);
    }
}