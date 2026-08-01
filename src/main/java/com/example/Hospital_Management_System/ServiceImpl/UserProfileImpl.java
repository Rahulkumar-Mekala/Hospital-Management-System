package com.example.Hospital_Management_System.ServiceImpl;

import java.io.IOException;
import java.io.ObjectInputFilter.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Hospital_Management_System.Enum.Role;
import com.example.Hospital_Management_System.ImageStorage.UserProfileStorage;
import com.example.Hospital_Management_System.Repository.EmailOtpRepository;
import com.example.Hospital_Management_System.Repository.UserRepository;
import com.example.Hospital_Management_System.Service.UserProfile;
import com.example.Hospital_Management_System.Service.UserService;
import com.example.Hospital_Management_System.Token.JwtService;
import com.example.Hospital_Management_System.dto.LoginRequest;
import com.example.Hospital_Management_System.dto.LoginResponce;
import com.example.Hospital_Management_System.entity.EmailOtp;
import com.example.Hospital_Management_System.entity.User;

import jakarta.transaction.Transactional;
@Service
public class UserProfileImpl implements UserProfile  {
	private final Map<String, User> tempUsers = new ConcurrentHashMap<>();
	private final UserRepository userRepository;
	private final UserProfileStorage storageService;
	private final EmailOtpRepository emailOtpRepository;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private  final JwtService jwtService;
	
	

	public UserProfileImpl(UserRepository userRepository, UserProfileStorage storageService,
			EmailOtpRepository emailOtpRepository, UserService userService, PasswordEncoder passwordEncoder,JwtService jwtService) {
		super();
		this.userRepository = userRepository;
		this.storageService = storageService;
		this.emailOtpRepository = emailOtpRepository;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.jwtService=jwtService;
	}


	@Override
	
	@Transactional
	public User saveProfile(User user, MultipartFile profile) throws IOException {

	  
	    user.setEmployeeCode(generateEmployeeCode());
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    if (user.getRole() == Role.DOCTOR) {
	        user.setLicenseNumber(generateLicenseNumber("DOCTOR"));
	    } else if (user.getRole() == Role.NURSE) {
	        user.setLicenseNumber(generateLicenseNumber("NURSE"));
	    }
	    if (profile != null && !profile.isEmpty()) {
	        String imageUrl = storageService.uploadProfileImage(profile);
	        user.setProfileImageUrl(imageUrl);
	    }

	    tempUsers.put(user.getEmail(), user);

	  
	    userService.sendRegistrationMail(user);

	    return user;
	}
	
	
	@Override
	@Transactional
	
	public String verifyOtp(String email, String otp) {
		
		 if (userRepository.existsByEmail(email)) {
		        return "Email is already registered.";
		    }
	    EmailOtp emailOtp = emailOtpRepository.findTopByEmailOrderByExpiryTimeDesc(email)
	            .orElseThrow(() -> new RuntimeException("OTP not found"));

	    if (!emailOtp.getOtp().equals(otp)) {
	        return "Invalid OTP.";
	    }

	    if (emailOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
	        return "OTP has expired.";
	    }

	    User user = tempUsers.get(email);

	    if (user == null) {
	        return "User registration data not found.";
	    }

	    user.setStatus("ACTIVE");

	    userRepository.save(user);

	    tempUsers.remove(email);

	    userService.sendVerificationSuccessMail(user);

	    return "Email verified successfully.";
	}
	
	@Override
	public LoginResponce login(String email, String password) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found."));

	    LoginResponce response = new LoginResponce();

	    if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
	        response.setMessage("Please verify your email first.");
	        return response;
	    }

	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        response.setMessage("Invalid password.");
	        return response;
	    }

	    String token = jwtService.generateToken(user);

	    response.setMessage("Login Successful");
	    response.setToken(token);

	    return response;
	}

	@Override
	public List<User> getAllPatients() {
	
		return null;
	}

	@Override
	public Optional<User> getPatientById(UUID id) {
	
		return Optional.empty();
	}

	@Override
	public void deletePatient(UUID id) {
	
		
	}

	@Override
	public Optional<User> findByEmail(String email) {
		
		return Optional.empty();
	}

	@Override
	public User updateUserEmail(String email, User patient, MultipartFile profile) throws IOException {
	
		return null;
	}
	
	 private String generateEmployeeCode() {
	        int year = LocalDate.now().getYear();
	        long count = userRepository.count() + 1;
	        return "EMP-" + year + "-" + String.format("%04d", count);
	    }
	  private String generateLicenseNumber(String role) {
		 
	        int year = LocalDate.now().getYear();
	        long count = userRepository.count() + 1;

	        if ("DOCTOR".equalsIgnoreCase(role)) {
	            return "DOC-" + year + "-" + String.format("%04d", count);
	        }

	        if ("NURSE".equalsIgnoreCase(role)) {
	            return "NUR-" + year + "-" + String.format("%04d", count);
	        }

	        return null;
	    }


	  @Override
	  @Transactional
	  public String forgotPassword(String email) {

	      User user = userRepository.findByEmail(email)
	              .orElseThrow(() -> new RuntimeException("Email is not registered"));

	     userService.sendForgotPasswordMail(user);

	      return "OTP sent successfully to your registered email.";
	  }


	  @Override
	  @Transactional
	  public String verifyForgotOtp(String email, String otp) {

	      EmailOtp emailOtp = emailOtpRepository
	              .findTopByEmailOrderByExpiryTimeDesc(email)
	              .orElseThrow(() -> new RuntimeException("OTP not found."));
	      if (!emailOtp.getOtp().equals(otp)) {
	          return "Invalid OTP.";
	      }

	      if (emailOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
	          return "OTP has expired.";
	      }

	      emailOtp.setVerified(true);
	      emailOtpRepository.save(emailOtp);

	      return "OTP verified successfully.";
	  }


	  @Override
	  @Transactional
	  public String changePassword(String email,
	                               String newPassword,
	                               String confirmPassword) {

	      // Check if passwords match
	      if (!newPassword.equals(confirmPassword)) {
	          return "New Password and Confirm Password do not match.";
	      }

	      EmailOtp emailOtp = emailOtpRepository
	              .findTopByEmailOrderByExpiryTimeDesc(email)
	              .orElseThrow(() -> new RuntimeException("OTP not found."));

	      if (!emailOtp.getVerified()) {
	          return "Please verify your OTP first.";
	      }

	      // Find user
	      User user = userRepository.findByEmail(email)
	              .orElseThrow(() -> new RuntimeException("User not found."));

	      user.setPassword(passwordEncoder.encode(newPassword));
	      userRepository.save(user);

	      emailOtpRepository.delete(emailOtp);
	      userService.sendPasswordChangedMail(user);

	      return "Password changed successfully.";
	  }


	  @Override
	  public String oldchangePassword(String email, String currentPassword, String newPassword, String confirmPassword) {
		  User user = userRepository.findByEmail(email)
		            .orElseThrow(() -> new RuntimeException("User not found"));

		    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
		        return "Current password is incorrect.";
		    }

		    if (!newPassword.equals(confirmPassword)) {
		        return "New Password and Confirm Password do not match.";
		    }


		    if (passwordEncoder.matches(newPassword, user.getPassword())) {
		        return "New password cannot be the same as the current password.";
		    }


		    user.setPassword(passwordEncoder.encode(newPassword));
		    userRepository.save(user);


		    userService.sendPasswordChangedMail(user);

		    return "Password changed successfully.";
		}
	

	
    
	
}
