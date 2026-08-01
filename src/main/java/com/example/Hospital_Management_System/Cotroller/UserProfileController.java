package com.example.Hospital_Management_System.Cotroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Hospital_Management_System.Enum.Qualification;
import com.example.Hospital_Management_System.Enum.Role;
import com.example.Hospital_Management_System.Enum.Specialization;
import com.example.Hospital_Management_System.ServiceImpl.UserProfileImpl;
import com.example.Hospital_Management_System.Token.JwtService;
import com.example.Hospital_Management_System.dto.Chang_Old_PasswordRequest;
import com.example.Hospital_Management_System.dto.ChangePasswordRequest;
import com.example.Hospital_Management_System.dto.ForgotPasswordRequest;
import com.example.Hospital_Management_System.dto.LoginRequest;
import com.example.Hospital_Management_System.dto.LoginResponce;
import com.example.Hospital_Management_System.dto.VerifyOtpRequest;
import com.example.Hospital_Management_System.entity.EmailOtp;
import com.example.Hospital_Management_System.entity.User;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileImpl userProfileService;
    private final JwtService jwtService;

  

    public UserProfileController(UserProfileImpl userProfileService, JwtService jwtService) {
		super();
		this.userProfileService = userProfileService;
		this.jwtService = jwtService;
	}

	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerUser(

            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam Role role,
            @RequestParam(required = false) Qualification qualification,
            @RequestParam(required = false) Specialization specialization,
            @RequestPart(value = "profile", required = false) MultipartFile profile)
            throws IOException {

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(role);
        user.setQualification(qualification);
        user.setSpecialization(specialization);

        userProfileService.saveProfile(user, profile);

        return ResponseEntity.ok("OTP sent successfully to your email.");
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponce> login(@RequestBody LoginRequest request) {

    	LoginResponce response = userProfileService.login(
                request.getEmail(),
                request.getPassword());

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/decode-token")
    public ResponseEntity<?> decodeToken(@RequestHeader("Authorization") String authorization) {


        String token = authorization.substring(7); // remove Bearer


        Claims claims = jwtService.decodeToken(token);


        Map<String, Object> response = new HashMap<>();
        response.put("id", claims.get("id"));
        response.put("employeeCode", claims.get("employeeCode"));
        
        response.put("firstName", claims.get("firstName"));
        response.put("lastname", claims.get("lastname"));
  
       response.put("profileImageUrl", claims.get("profileImageUrl"));
        response.put("email", claims.getSubject());
        response.put("role", claims.get("role"));
       
        response.put("phone", claims.get("phone"));
        response.put("status", claims.get("status"));


        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        String message = userProfileService.forgotPassword(request.getEmail());

        return ResponseEntity.ok(message);
    }


@PostMapping("/verify-forgot-otp")
public ResponseEntity<String> verifyForgotOtp(@RequestBody VerifyOtpRequest request) {

    String message = userProfileService.verifyForgotOtp(
            request.getEmail(),
            request.getOtp());

  return ResponseEntity.ok(message);
 }

@PostMapping("/change-password")
public ResponseEntity<String> changePassword(
        @RequestBody ChangePasswordRequest request) {

    String message = userProfileService.changePassword(
            request.getEmail(),
            request.getNewPassword(),
            request.getConfirmPassword());

    return ResponseEntity.ok(message);
}
@PostMapping("/old-change-password")
public ResponseEntity<String> oldchangePassword(
        @RequestBody Chang_Old_PasswordRequest request) {

    return ResponseEntity.ok(
            userProfileService.oldchangePassword(
                    request.getEmail(),
                    request.getCurrentPassword(),
                    request.getNewPassword(),
                    request.getConfirmPassword()));
}
} 