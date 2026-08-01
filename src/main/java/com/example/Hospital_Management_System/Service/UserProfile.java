package com.example.Hospital_Management_System.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.Hospital_Management_System.dto.LoginResponce;
import com.example.Hospital_Management_System.entity.User;

public interface UserProfile  {

	 User saveProfile(User user, MultipartFile profile) throws IOException;
	 String verifyOtp(String email, String otp);
	 LoginResponce login(String email, String password);
	    List<User> getAllPatients();
	    Optional<User> getPatientById(UUID id);
	    void deletePatient(UUID id);
	    Optional<User> findByEmail(String email);
	    User updateUserEmail(String email, User patient,MultipartFile profile) throws IOException;
	   
	    String forgotPassword(String email);	 
	    String verifyForgotOtp(String email, String otp);
	    String changePassword(String email, String newPassword, String confirmPassword);
	    String oldchangePassword(String email,String currentPassword,String newPassword,String confirmPassword);
}
