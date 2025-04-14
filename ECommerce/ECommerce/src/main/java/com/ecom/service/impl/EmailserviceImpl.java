package com.ecom.service.impl;

import java.util.Optional;
import org.springframework.mail.javamail.MimeMessageHelper;
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;

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
import com.ecom.service.EmailService;
import com.ecom.service.TokenService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailserviceImpl implements EmailService {
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

	public void sendTestEmail() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(sender);
		message.setTo("dharmeshgelatardhirajlal@gmail.com");
		message.setSubject("Test email from Spring Boot");
		message.setText("This is a test email from your application");
		mailSender.send(message);
	}

	public String sendPasswordResetEmail(String toEmail) {

		User user = userdao.findByEmail(toEmail);
		if (user == null) {
			return "UNF";
		}

		String resetToken = tokenService.generatePasswordResetToken(user.getEmail(), user.getUserPassword());
		String resetLink = "http://localhost:4200/reset-password?token=" + resetToken;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(sender);
		message.setTo(toEmail);
		message.setSubject("Password Reset Request");
		message.setText("Hello " + user.getUserName() + ",\n\n"
				+ "You requested to reset your password. Please click the link below to reset your password:\n\n"
				+ resetLink + "\n\n" + "This link will expire in 10 minutes.\n\n"
				+ "If you did not request a password reset, please ignore this email.\n\n"
				+ "Thank you,\nYour Application Team");

		System.out.println("Hello " + user.getUserName() + ",\n\n"
				+ "You requested to reset your password. Please click the link below to reset your password:\n\n"
				+ resetLink + "\n\n" + "This link will expire in 10 minutes.\n\n"
				+ "If you did not request a password reset, please ignore this email.\n\n"
				+ "Thank you,\nYour Application Team");

		mailSender.send(message);
		return "S";

	}

	public boolean resetPassword(String email, String newPassword) {
		try {
			User user = userdao.findByEmail(email);
			if (user == null) {
				System.err.println("user not found");
				return false;
			}

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

	@Override
	public String sendEmailForRole(String username, String toEmail) {
		System.err.println("sendPasswordForRole service");
		User user = userdao.findByUserName(username).orElse(null);
		if (user == null) {
			return "UNF";
		}

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(message, true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		try {
			helper.setFrom(sender);
			helper.setTo(toEmail);
			helper.setSubject("Request to Register as Seller");

			String htmlContent = "<html>" + "<body>" + "<h2>Dear Admin,</h2>"
					+ "<p>I hope this message finds you well.</p>" + "<p>My name is <strong>" + user.getUserName()
					+ "</strong>, and I am writing to request registration for a seller account on your website.</p>"
					+ "<p>I am eager to start selling and contributing to your platform.</p>"
					+ "<p>Please let me know if you require any further information or documentation to process my request.</p>"
					+ "<p>Thank you for your attention to this matter.</p>" + "<p>Best regards,<br>"
					+ user.getUserName() + "<br>" + toEmail + "</p>" + "</body>" + "</html>";

			helper.setText(htmlContent, true);
			System.out.println("Dear Admin I hope this message finds you well My name is  " + user.getUserName()
					+ "and I am writing to request registration for a seller account on your website.I am eager to start selling and contributing to your platform.Please let me know if you require any further information or documentation to process my requestThank you for your attention to this matter.Best regards,"
					+ user.getUserName());

			mailSender.send(message);
			return "S";
		} catch (MessagingException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

	@Override
	public String sendEmailToUser(String username, String role) {
		User user = userdao.findByUserName(username).get();
		if (user == null) {
			return "UNF"; // Ensure this matches the frontend check
		}
		System.out.println(user.getEmail());

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(sender);
		message.setTo(user.getEmail());
		message.setSubject("Registration as Seller Approved");
		message.setText("Dear " + username + user.getEmail() + ",\n\n"
				+ "We are pleased to inform you that your request to register as a seller has been approved by the admin.\n\n"
				+ "Your current role is: " + role + ".\n\n"
				+ "You can now start selling on our platform. If you have any questions or need further assistance, please feel free to reach out.\n\n"
				+ "Thank you for being a part of our community.\n\n" + "Best regards,\n" + "The Admin Team");

		System.out.println("Dear " + username + ",\n\n"
				+ "We are pleased to inform you that your request to register as a seller has been approved by the admin.\n\n"
				+ "Your current role is: " + role + ".\n\n"
				+ "You can now start selling on our platform. If you have any questions or need further assistance, please feel free to reach out.\n\n"
				+ "Thank you for being a part of our community.\n\n" + "Best regards,\n" + "The Admin Team");
		mailSender.send(message);
		return "S"; //
	}

}
