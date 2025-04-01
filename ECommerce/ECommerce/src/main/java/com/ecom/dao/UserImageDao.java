package com.ecom.dao;

import com.ecom.entity.UserImage;
import com.ecom.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



@Repository
public interface UserImageDao extends CrudRepository<UserImage, Long> {
    Optional<UserImage> findByUser(User user);
}