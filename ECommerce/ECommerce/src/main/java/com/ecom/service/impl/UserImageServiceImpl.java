package com.ecom.service.impl;

import com.ecom.dao.UserImageDao;
import com.ecom.dao.UserDao;
import com.ecom.entity.UserImage;
import com.ecom.service.UserImageService;
import com.ecom.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserImageServiceImpl implements UserImageService{

    @Autowired
    private UserImageDao userImageRepository;
    
    @Autowired
    private UserDao userDao;
    
    public UserImage uploadImage(String userName, MultipartFile file) throws IOException {
        User user = userDao.findById(userName).orElseThrow(() -> 
            new RuntimeException("User not found with username: " + userName));
        
        // Check if user already has an image
        Optional<UserImage> existingImage = userImageRepository.findByUser(user);
        UserImage userImage;
        
        if (existingImage.isPresent()) {
            userImage = existingImage.get();
            userImage.setImageData(file.getBytes());
        } else {
            userImage = new UserImage();
            userImage.setUser(user);
            userImage.setImageData(file.getBytes());
        }
        
        return userImageRepository.save(userImage);
    }
    
    public byte[] getImageByUserName(String userName) {
        User user = userDao.findById(userName).orElseThrow(() -> 
            new RuntimeException("User not found with username: " + userName));
        
        Optional<UserImage> userImage = userImageRepository.findByUser(user);
        
        return userImage.map(UserImage::getImageData).orElse(null);
    }
}