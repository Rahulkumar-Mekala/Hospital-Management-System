package com.example.Hospital_Management_System.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.Hospital_Management_System.Repository.EmailOtpRepository;
import com.example.Hospital_Management_System.Service.UserService;
import com.example.Hospital_Management_System.entity.EmailOtp;
import com.example.Hospital_Management_System.entity.User;

import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {
	
	private  final EmailOtpRepository emailOtpRepository;
	    private  final JavaMailSender mailSender;

	
	public UserServiceImpl(EmailOtpRepository emailOtpRepository, JavaMailSender mailSender) {
			super();
			this.emailOtpRepository = emailOtpRepository;
			this.mailSender = mailSender;
		}

	@Override
	public void sendRegistrationMail(User user) {
 
	    try {
	    	 String otp = generateOtp();

	    	    emailOtpRepository.deleteByEmail(user.getEmail());

	    	    EmailOtp emailOtp = new EmailOtp();
	    	    emailOtp.setEmail(user.getEmail());
	    	    emailOtp.setOtp(otp);
	    	    emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
	    	    emailOtp.setVerified(false);

	    	    emailOtpRepository.save(emailOtp);

	    	    System.out.println("OTP Saved Successfully");

	        MimeMessage message = mailSender.createMimeMessage();

	        MimeMessageHelper helper = new MimeMessageHelper(
	                message,
	                true,
	                "UTF-8"
	        );

	        helper.setTo(user.getEmail());
	        helper.setSubject("Registration Successful");

	        String htmlContent = """
	                <!DOCTYPE html>
	                <html>
	                <head>
	                    <style>
	                        body {
	                            font-family: Arial, sans-serif;
	                            background-color: #f4f6f8;
	                            padding: 20px;
	                        }

	                        .container {
	                            max-width: 600px;
	                            margin: auto;
	                            background: white;
	                            padding: 30px;
	                            border-radius: 10px;
	                            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
	                        }

	                        .header {
	                            background-color: #1976d2;
	                            color: white;
	                            padding: 15px;
	                            text-align: center;
	                            border-radius: 10px 10px 0 0;
	                        }

	                        .otp {
	                            font-size: 28px;
	                            font-weight: bold;
	                            color: #1976d2;
	                            text-align: center;
	                            margin: 20px;
	                            letter-spacing: 5px;
	                        }

	                        .message {
	                            font-size: 16px;
	                            color: #333;
	                            line-height: 1.6;
	                        }

	                        .footer {
	                            margin-top: 20px;
	                            font-size: 12px;
	                            color: gray;
	                            text-align: center;
	                        }

	                    </style>
	                </head>

	                <body>

	                <div class="container">

	                    <div class="header">
	                        <h2>Registration Successful</h2>
	                    </div>


	                    <div class="message">

	                        <p>
	                            Dear <b>%s</b>,
	                        </p>

	                        <p>
	                            Welcome to our Hospital Management System.
	                            Your registration has been completed successfully.
	                        </p>

	                        <p>
	                            Your OTP for account verification is:
	                        </p>


	                        <div class="otp">
	                            %s
	                        </div>


	                        <p>
	                            This OTP is valid for <b>5 minutes</b>.
	                        </p>


	                        <p>
	                            Please do not share this OTP with anyone.
	                        </p>

	                    </div>


	                    <div class="footer">
	                        © 2026 Hospital Management System
	                    </div>

	                </div>

	                </body>
	                </html>
	                """.formatted(
	                        user.getFirstName(),
	                        otp
	                );


	        helper.setText(htmlContent, true);

	        mailSender.send(message);


	    } catch (Exception e) {
	        throw new RuntimeException("Failed to send registration email", e);
	    }
	}

	@Override
	public String generateOtp() {
		 return String.format("%06d", new Random().nextInt(1000000));
	}
	@Override
	public void sendVerificationSuccessMail(User user) {

	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	        helper.setTo(user.getEmail());
	        helper.setSubject("✅ Email Verified Successfully");

	        String html = """
	                <!DOCTYPE html>
	                <html>
	                <head>
	                    <style>
	                        body{
	                            margin:0;
	                            padding:0;
	                            background:#f4f6f9;
	                            font-family:Arial,Helvetica,sans-serif;
	                        }
	                        .container{
	                            width:600px;
	                            margin:30px auto;
	                            background:#ffffff;
	                            border-radius:10px;
	                            overflow:hidden;
	                            box-shadow:0 5px 15px rgba(0,0,0,.15);
	                        }
	                        .header{
	                            background:#28a745;
	                            color:#fff;
	                            text-align:center;
	                            padding:25px;
	                            font-size:28px;
	                            font-weight:bold;
	                        }
	                        .content{
	                            padding:35px;
	                            color:#333;
	                            font-size:16px;
	                            line-height:1.8;
	                        }
	                        .success{
	                            text-align:center;
	                            font-size:60px;
	                            color:#28a745;
	                        }
	                        .button{
	                            display:inline-block;
	                            margin-top:20px;
	                            padding:12px 30px;
	                            background:#28a745;
	                            color:#ffffff !important;
	                            text-decoration:none;
	                            border-radius:5px;
	                            font-weight:bold;
	                        }
	                        .footer{
	                            background:#f1f1f1;
	                            text-align:center;
	                            padding:20px;
	                            font-size:13px;
	                            color:#777;
	                        }
	                    </style>
	                </head>
	                <body>

	                <div class="container">

	                    <div class="header">
	                        Hospital Management System
	                    </div>

	                    <div class="content">

	                        <div class="success">✔</div>

	                        <h2>Hello, %s</h2>

	                        <p>
	                            Congratulations! Your email has been verified successfully.
	                        </p>

	                        <p>
	                            Your account is now <b style="color:green;">ACTIVE</b>.
	                            You can now log in and access the Hospital Management System.
	                        </p>

	                        <center>
	                            <a href="http://localhost:3000/login" class="button">
	                                Login Now
	                            </a>
	                        </center>

	                        <p style="margin-top:30px;">
	                            Thank you for choosing our services.
	                        </p>

	                    </div>

	                    <div class="footer">
	                        © 2026 Hospital Management System <br>
	                        This is an automated email. Please do not reply.
	                    </div>

	                </div>

	                </body>
	                </html>
	                """.formatted(user.getFirstName());

	        helper.setText(html, true);

	        mailSender.send(message);

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to send verification email", e);
	    }
	}

	@Override
	public void sendForgotPasswordMail(User user) {

	    try {

	        String otp = generateOtp();

	        
	        emailOtpRepository.deleteByEmail(user.getEmail());

	        // Save new OTP
	        EmailOtp emailOtp = new EmailOtp();
	        emailOtp.setEmail(user.getEmail());
	        emailOtp.setOtp(otp);
	        emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
	        emailOtp.setVerified(false);

	        emailOtpRepository.save(emailOtp);

	        System.out.println("Forgot Password OTP Saved Successfully");

	        MimeMessage message = mailSender.createMimeMessage();

	        MimeMessageHelper helper = new MimeMessageHelper(
	                message,
	                true,
	                "UTF-8"
	        );

	        helper.setTo(user.getEmail());
	        helper.setSubject("Forgot Password OTP");

	        String htmlContent = """
	                <!DOCTYPE html>
	                <html>
	                <head>
	                    <style>
	                        body{
	                            font-family:Arial,sans-serif;
	                            background:#f4f6f8;
	                            padding:20px;
	                        }

	                        .container{
	                            max-width:600px;
	                            margin:auto;
	                            background:#fff;
	                            padding:30px;
	                            border-radius:10px;
	                            box-shadow:0 2px 10px rgba(0,0,0,.1);
	                        }

	                        .header{
	                            background:#d32f2f;
	                            color:white;
	                            padding:15px;
	                            text-align:center;
	                            border-radius:10px 10px 0 0;
	                        }

	                        .otp{
	                            font-size:30px;
	                            font-weight:bold;
	                            color:#d32f2f;
	                            text-align:center;
	                            letter-spacing:6px;
	                            margin:20px;
	                        }

	                        .footer{
	                            text-align:center;
	                            color:gray;
	                            margin-top:20px;
	                            font-size:12px;
	                        }
	                    </style>
	                </head>

	                <body>

	                <div class="container">

	                    <div class="header">
	                        <h2>Forgot Password</h2>
	                    </div>

	                    <p>Dear <b>%s</b>,</p>

	                    <p>
	                        We received a request to reset your password.
	                    </p>

	                    <p>Your OTP is:</p>

	                    <div class="otp">%s</div>

	                    <p>
	                        This OTP is valid for <b>5 minutes</b>.
	                    </p>

	                    <p>
	                        If you did not request a password reset,
	                        please ignore this email.
	                    </p>

	                    <div class="footer">
	                        © 2026 Hospital Management System
	                    </div>

	                </div>

	                </body>
	                </html>
	                """.formatted(
	                user.getFirstName(),
	                otp
	        );

	        helper.setText(htmlContent, true);

	        mailSender.send(message);

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to send forgot password email", e);
	    }
	}

	@Override
	public void sendPasswordChangedMail(User user) {

	    try {

	        MimeMessage message = mailSender.createMimeMessage();

	        MimeMessageHelper helper = new MimeMessageHelper(
	                message,
	                true,
	                "UTF-8"
	        );

	        helper.setTo(user.getEmail());
	        helper.setSubject("Password Changed Successfully");

	        String html = """
	                <!DOCTYPE html>
	                <html>
	                <body style="font-family:Arial,sans-serif;background:#f4f6f8;padding:20px;">

	                <div style="max-width:600px;
	                            margin:auto;
	                            background:white;
	                            padding:30px;
	                            border-radius:10px;
	                            box-shadow:0 0 10px #ccc;">

	                    <h2 style="color:#28a745;">
	                        Password Changed Successfully
	                    </h2>

	                    <p>Dear <b>%s</b>,</p>

	                    <p>Your password has been changed successfully.</p>

	                    <p>
	                        If you made this change, no further action is required.
	                    </p>

	                    <p>
	                        If you did NOT change your password,
	                        please contact the administrator immediately.
	                    </p>

	                    <br>

	                    <p>Regards,</p>

	                    <p><b>Hospital Management System</b></p>

	                </div>

	                </body>
	                </html>
	                """.formatted(user.getFirstName());

	        helper.setText(html, true);

	        mailSender.send(message);

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to send password changed email", e);
	    }
	}



}
