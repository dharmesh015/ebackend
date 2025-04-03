package com.ecom.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecom.dao.UserDao;
import com.ecom.entity.User;

@Service
public class Emailservice {
	@Autowired
	private UserDao userdao;
	
	@Value("${spring.mail.username}")
	private String sender;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenService tokenService;

//    public void sendEmail() {
//    	
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(sender);
//        message.setTo("dharmeshgelatardhirajlal@gmail.com");
//        message.setSubject("check to send email by spring boot");
//        message.setText("done it reach to you");
//
//        mailSender.send(message);
//    }
//    
//    public String sendEmail(String toEmail) {
//        User byEmail = userdao.findByEmail(toEmail);
//
//        if (byEmail == null) {
//            return "User  not found"; // Return a specific message
//        }
//        
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject("Password Reset Request");
//        message.setText("Please click the link to reset your password.");
//        mailSender.send(message);
//        
//        return "Successfully sent email"; // Return a success message
//    }
    
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo("dharmeshgelatardhirajlal@gmail.com");
        message.setSubject("Test email from Spring Boot");
        message.setText("This is a test email from your application");
        mailSender.send(message);
    }
    
    public String sendPasswordResetEmail(String toEmail) {
        try {
            User user = userdao.findByEmail(toEmail);
            if (user == null) {
                System.err.println("User  not found");
                return "User  not found";
            }
            
            // Generate a unique reset link or token
            String resetToken = tokenService.generatePasswordResetToken(user.getEmail());
            System.err.println("Generated Token: " + resetToken);
            
            // Validate the token immediately after generation
            String validatedEmail = tokenService.validateToken(resetToken);
            System.err.println("Validated Email: " + validatedEmail);
            
            String resetLink = "http://localhost:4200/reset-password?token=" + resetToken;
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request");
            message.setText("Hello " + user.getUserName() + ",\n\n" +
                    "You requested to reset your password. Please click the link below to reset your password:\n\n" +
                    resetLink + "\n\n" +
                    "This link will expire in 10 minutes.\n\n" + // Update to 10 minutes
                    "If you did not request a password reset, please ignore this email.\n\n" +
                    "Thank you,\nYour Application Team");
            
            mailSender.send(message);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    
    
    
    public boolean resetPassword(String email, String newPassword) {
        try {
            User user = userdao.findByEmail(email);
            if (user == null) {
            System.err.println("user not found");
                return false;
            }
            
            // Update user's password
            // Note: In production, you should encrypt the password
            user.setUserPassword(getEncodedPassword(newPassword));
            userdao.save(user);
            System.err.println("user  found");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
	
}
