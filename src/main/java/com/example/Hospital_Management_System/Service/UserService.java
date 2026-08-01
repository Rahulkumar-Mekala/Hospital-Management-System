package com.example.Hospital_Management_System.Service;

import com.example.Hospital_Management_System.entity.User;

public interface UserService {
	
	  void sendRegistrationMail(User user);
	  String generateOtp();
	  void sendVerificationSuccessMail(User user);
	  void sendForgotPasswordMail(User user);
	  void sendPasswordChangedMail(User user);
}
