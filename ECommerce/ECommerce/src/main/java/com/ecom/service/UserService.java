package com.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.dao.RoleDao;
import com.ecom.dao.UserDao;
import com.ecom.entity.Role;
import com.ecom.entity.User;

import jakarta.annotation.PostConstruct;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initRoleAndUser () {
        try {
            Role sellerRole = new Role();
            sellerRole.setRoleName("Seller");
            sellerRole.setRoleDescription("Seller role");
            roleDao.save(sellerRole);

            Role adminRole = new Role();
            adminRole.setRoleName("Admin");
            adminRole.setRoleDescription("Admin role");
            roleDao.save(adminRole);

            Role userRole = new Role();
            userRole.setRoleName("User");
            userRole.setRoleDescription("Default role for newly created record");
            roleDao.save(userRole);

            User sellerUser = new User();
            sellerUser.setUserName("dharmesh123");
            sellerUser.setUserPassword(getEncodedPassword("seller"));
            sellerUser.setUserFirstName("dharmesh");
            sellerUser.setUserLastName("gelatar");
            Set<Role> roles = new HashSet<>();
            roles.add(sellerRole);
            sellerUser .setRole(roles);
            userDao.save(sellerUser );

            User adminUser  = new User();
            adminUser.setUserName("deep123");
            adminUser.setUserPassword(getEncodedPassword("admin"));
            adminUser.setUserFirstName("deep");
            adminUser.setUserLastName("patel");
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminUser .setRole(adminRoles);
            userDao.save(adminUser );
        } catch (Exception e) {
            // Log the error
            System.err.println("Error initializing roles and users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public User registerNewUser (User user) {
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));

        User savedUser  = userDao.save(user);

        Role role = roleDao.findById("User").orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        savedUser .setRole(userRoles);

        return userDao.save(savedUser);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}