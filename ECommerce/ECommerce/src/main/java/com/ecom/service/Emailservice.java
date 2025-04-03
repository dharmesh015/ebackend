package com.ecom.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Emailservice {
	
	@Value("${spring.mail.username}")
	private String sender;
	
	@Autowired
    private JavaMailSender mailSender;

    public void sendEmail() {
    	
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo("dharmeshgelatardhirajlal@gmail.com");
        message.setSubject("check to send email by spring boot");
        message.setText("done it reach to you");

        mailSender.send(message);
    }
    
    public void sendEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Please click the link to reset your password.");
        mailSender.send(message);
    }

}
