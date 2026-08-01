package com.example.Hospital_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chang_Old_PasswordRequest {
	 private String email;
	    private String currentPassword;
	    private String newPassword;
	    private String confirmPassword;
}
