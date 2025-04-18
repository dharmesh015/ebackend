package com.ecom.service;

import org.springframework.stereotype.Service;

import com.ecom.entity.OrderDetail;

public interface EmailService {
	void sendTestEmail();

	String sendPasswordResetEmail(String toEmail);

	boolean resetPassword(String email, String newPassword);

	String getEncodedPassword(String password);

	String sendEmailForRole(String username, String toEmail);

	String sendEmailToUser(String username, String toEmail);

	String sendOrderConfirmationEmail(OrderDetail orderDetail, byte[] pdfBytes);

}
