package com.example.Hospital_Management_System.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.Hospital_Management_System.GmailToken.GmailOAuthService;
import com.example.Hospital_Management_System.Repository.EmailOtpRepository;
import com.example.Hospital_Management_System.Service.UserService;
import com.example.Hospital_Management_System.entity.EmailOtp;
import com.example.Hospital_Management_System.entity.User;

@Service
public class UserServiceImpl implements UserService {

private final EmailOtpRepository emailOtpRepository;
private final GmailOAuthService mailSender;

public UserServiceImpl(
        EmailOtpRepository emailOtpRepository,
        GmailOAuthService mailSender) {

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

        mailSender.sendEmail(
                user.getEmail(),
                "Registration Successful",
                htmlContent
        );

    } catch (Exception e) {
        throw new RuntimeException(
                "Failed to send registration email",
                e
        );
    }
}

@Override
public String generateOtp() {
    return String.format(
            "%06d",
            new Random().nextInt(1000000)
    );
}

@Override
public void sendVerificationSuccessMail(User user) {

    try {

        String html = """
                <!DOCTYPE html>
                <html>
                <body style="font-family:Arial,sans-serif;background:#f4f6f9;padding:20px;">

                <div style="max-width:600px;
                            margin:auto;
                            background:white;
                            padding:30px;
                            border-radius:10px;">

                    <h2 style="color:#28a745;">
                        Email Verified Successfully
                    </h2>

                    <p>Hello <b>%s</b>,</p>

                    <p>
                        Congratulations! Your email has been verified successfully.
                    </p>

                    <p>
                        Your account is now
                        <b style="color:green;">ACTIVE</b>.
                    </p>

                    <p>
                        You can now log in and access the Hospital Management System.
                    </p>

                    <p>
                        Thank you for choosing our services.
                    </p>

                    <p>
                        Regards,<br>
                        <b>Hospital Management System</b>
                    </p>

                </div>

                </body>
                </html>
                """.formatted(user.getFirstName());

        mailSender.sendEmail(
                user.getEmail(),
                "Email Verified Successfully",
                html
        );

    } catch (Exception e) {
        throw new RuntimeException(
                "Failed to send verification email",
                e
        );
    }
}

@Override
public void sendForgotPasswordMail(User user) {

    try {

        String otp = generateOtp();

        emailOtpRepository.deleteByEmail(user.getEmail());

        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setEmail(user.getEmail());
        emailOtp.setOtp(otp);
        emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        emailOtp.setVerified(false);

        emailOtpRepository.save(emailOtp);

        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <body style="font-family:Arial,sans-serif;
                             background:#f4f6f8;
                             padding:20px;">

                <div style="max-width:600px;
                            margin:auto;
                            background:#fff;
                            padding:30px;
                            border-radius:10px;">

                    <h2 style="color:#d32f2f;">
                        Forgot Password
                    </h2>

                    <p>
                        Dear <b>%s</b>,
                    </p>

                    <p>
                        We received a request to reset your password.
                    </p>

                    <p>
                        Your OTP is:
                    </p>

                    <div style="font-size:30px;
                                font-weight:bold;
                                color:#d32f2f;
                                text-align:center;
                                letter-spacing:6px;
                                margin:20px;">
                        %s
                    </div>

                    <p>
                        This OTP is valid for <b>5 minutes</b>.
                    </p>

                    <p>
                        If you did not request a password reset,
                        please ignore this email.
                    </p>

                    <div style="text-align:center;
                                color:gray;
                                margin-top:20px;
                                font-size:12px;">
                        © 2026 Hospital Management System
                    </div>

                </div>

                </body>
                </html>
                """.formatted(
                        user.getFirstName(),
                        otp
                );

        mailSender.sendEmail(
                user.getEmail(),
                "Forgot Password OTP",
                htmlContent
        );

    } catch (Exception e) {
        throw new RuntimeException(
                "Failed to send forgot password email",
                e
        );
    }
}

@Override
public void sendPasswordChangedMail(User user) {

    try {

        String html = """
                <!DOCTYPE html>
                <html>
                <body style="font-family:Arial,sans-serif;
                             background:#f4f6f8;
                             padding:20px;">

                <div style="max-width:600px;
                            margin:auto;
                            background:white;
                            padding:30px;
                            border-radius:10px;">

                    <h2 style="color:#28a745;">
                        Password Changed Successfully
                    </h2>

                    <p>
                        Dear <b>%s</b>,
                    </p>

                    <p>
                        Your password has been changed successfully.
                    </p>

                    <p>
                        If you made this change, no further action is required.
                    </p>

                    <p>
                        If you did NOT change your password,
                        please contact the administrator immediately.
                    </p>

                    <br>

                    <p>
                        Regards,
                    </p>

                    <p>
                        <b>Hospital Management System</b>
                    </p>

                </div>

                </body>
                </html>
                """.formatted(user.getFirstName());

        mailSender.sendEmail(
                user.getEmail(),
                "Password Changed Successfully",
                html
        );

    } catch (Exception e) {
        throw new RuntimeException(
                "Failed to send password changed email",
                e
        );
    }
}

}
