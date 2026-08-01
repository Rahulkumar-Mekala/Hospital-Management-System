package com.example.Hospital_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
	 private String email;
	    private String newPassword;
	    private String confirmPassword;
}
