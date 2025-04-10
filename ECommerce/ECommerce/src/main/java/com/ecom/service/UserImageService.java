package com.ecom.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ecom.entity.UserImage;

public interface UserImageService {
	UserImage uploadImage(String userName, MultipartFile file) throws IOException;
    byte[] getImageByUserName(String userName);
}
