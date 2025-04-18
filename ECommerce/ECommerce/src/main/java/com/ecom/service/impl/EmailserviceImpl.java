package com.ecom.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.ecom.entity.OrderDetail;
import com.ecom.entity.Product;
import com.ecom.entity.User;
import com.ecom.service.EmailService;
import com.ecom.service.TokenService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

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

	public String sendOrderConfirmationEmail(OrderDetail orderDetail, byte[] pdfBytes) {
		try {
			// Get user from order detail
			User user = orderDetail.getUser();
			if (user == null) {
				return "User not found";
			}

			String email = user.getEmail();
			if (email == null || email.isEmpty()) {
				return "User email not available";
			}

			// Create a MIME message for attachment support
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			// Set email properties
			helper.setFrom(sender);
			helper.setTo(email);
			helper.setSubject("Order Confirmation - Your Order #" + generateOrderNumber(orderDetail));

			// Prepare HTML content for email body
			String emailContent = buildEmailContent(orderDetail);
			helper.setText(emailContent, true); // true indicates HTML content

			// Add PDF attachment
			helper.addAttachment("OrderBill_" + generateOrderNumber(orderDetail) + ".pdf",
					new ByteArrayDataSource(pdfBytes, "application/pdf"));

			// Send email
			mailSender.send(message);

			return "Order confirmation email sent successfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to send order confirmation email: " + e.getMessage();
		}
	}

	/**
	 * Builds HTML content for the order confirmation email
	 */
	private String buildEmailContent(OrderDetail orderDetail) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		String orderDate = dateFormat.format(new Date());

		Product product = orderDetail.getProduct();
		String productName = product.getProductName();
		double price = product.getProductDiscountedPrice();
		int quantity = (int) (orderDetail.getOrderAmount() / price);

		StringBuilder emailBuilder = new StringBuilder();
		emailBuilder.append("<html><body>");
		emailBuilder.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");

		// Header
		emailBuilder.append("<div style='background-color:  #5980c1; padding: 20px; text-align: center;'>");
		emailBuilder.append("<h1 style='color: white; margin: 0;'>Order Confirmation</h1>");
		emailBuilder.append("</div>");

		// Order info
		emailBuilder.append("<div style='padding: 20px; border: 1px solid #ddd; background-color: #f9f9f9;'>");
		emailBuilder.append("<h2>Thank you for your order!</h2>");
		emailBuilder
				.append("<p>We're pleased to confirm that your order has been received and is being processed.</p>");

		emailBuilder.append("<div style='margin-top: 20px;'>");
		emailBuilder.append(
				"<h3 style='color: #228B22; border-bottom: 1px solid #ddd; padding-bottom: 10px;'>Order Summary</h3>");
		emailBuilder.append("<p><strong>Order Number:</strong> #").append(generateOrderNumber(orderDetail))
				.append("</p>");
		emailBuilder.append("<p><strong>Order Date:</strong> ").append(orderDate).append("</p>");
		emailBuilder.append("<p><strong>Order Status:</strong> ").append(orderDetail.getOrderStatus()).append("</p>");
		emailBuilder.append("</div>");

		// Customer details
		emailBuilder.append("<div style='margin-top: 20px;'>");
		emailBuilder.append(
				"<h3 style='color: #228B22; border-bottom: 1px solid #ddd; padding-bottom: 10px;'>Customer Information</h3>");
		emailBuilder.append("<p><strong>Name:</strong> ").append(orderDetail.getOrderFullName()).append("</p>");
//        emailBuilder.append("<p><strong>Shipping Address:</strong> ").append(orderDetail.getFullAddress()).append("</p>");
		emailBuilder.append("<p><strong>Contact:</strong> ").append(orderDetail.getOrderContactNumber()).append("</p>");
		if (orderDetail.getOrderAlternateContactNumber() != null
				&& !orderDetail.getOrderAlternateContactNumber().isEmpty()) {
			emailBuilder.append("<p><strong>Alternate Contact:</strong> ")
					.append(orderDetail.getOrderAlternateContactNumber()).append("</p>");
		}
		emailBuilder.append("</div>");

		// Product details
		emailBuilder.append("<div style='margin-top: 20px;'>");
		emailBuilder.append(
				"<h3 style='color: #228B22; border-bottom: 1px solid #ddd; padding-bottom: 10px;'>Order Items</h3>");

		// Table for product details
		emailBuilder.append("<table style='width: 100%; border-collapse: collapse;'>");
		emailBuilder.append("<tr style='background-color: #f2f2f2;'>");
		emailBuilder.append("<th style='padding: 10px; text-align: left; border: 1px solid #ddd;'>Product</th>");
		emailBuilder.append("<th style='padding: 10px; text-align: center; border: 1px solid #ddd;'>Quantity</th>");
		emailBuilder.append("<th style='padding: 10px; text-align: right; border: 1px solid #ddd;'>Price</th>");
		emailBuilder.append("<th style='padding: 10px; text-align: right; border: 1px solid #ddd;'>Total</th>");
		emailBuilder.append("</tr>");

		// Product row
		emailBuilder.append("<tr>");
		emailBuilder.append("<td style='padding: 10px; text-align: left; border: 1px solid #ddd;'>").append(productName)
				.append("</td>");
		emailBuilder.append("<td style='padding: 10px; text-align: center; border: 1px solid #ddd;'>").append(quantity)
				.append("</td>");
		emailBuilder.append("<td style='padding: 10px; text-align: right; border: 1px solid #ddd;'>$")
				.append(String.format("%.2f", price)).append("</td>");
		emailBuilder.append("<td style='padding: 10px; text-align: right; border: 1px solid #ddd;'>$")
				.append(String.format("%.2f", orderDetail.getOrderAmount())).append("</td>");
		emailBuilder.append("</tr>");

		// Order total
		emailBuilder.append("<tr>");
		emailBuilder.append(
				"<td colspan='3' style='padding: 10px; text-align: right; border: 1px solid #ddd;'><strong>Total:</strong></td>");
		emailBuilder.append("<td style='padding: 10px; text-align: right; border: 1px solid #ddd;'><strong>$")
				.append(String.format("%.2f", orderDetail.getOrderAmount())).append("</strong></td>");
		emailBuilder.append("</tr>");
		emailBuilder.append("</table>");
		emailBuilder.append("</div>");

		// Note about PDF
		emailBuilder.append(
				"<div style='margin-top: 20px; padding: 15px; background-color: #f0f8ff; border-left: 4px solid #228B22;'>");
		emailBuilder.append("<p><strong>Note:</strong> Your order bill is attached to this email as a PDF file.</p>");
		emailBuilder.append("</div>");

		// Footer
		emailBuilder.append(
				"<div style='margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd; text-align: center; color: #666;'>");
		emailBuilder.append("<p>If you have any questions about your order, please contact our customer service.</p>");
		emailBuilder.append("<p>Thank you for shopping with us!</p>");
		emailBuilder.append("</div>");

		emailBuilder.append("</div>");
		emailBuilder.append("</body></html>");

		return emailBuilder.toString();
	}

	// Generate an order number from the order ID
	private String generateOrderNumber(OrderDetail orderDetail) {
		if (orderDetail.getOrderId() != null) {
			return String.format("%08d", orderDetail.getOrderId());
		} else {
			return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		}
	}
}