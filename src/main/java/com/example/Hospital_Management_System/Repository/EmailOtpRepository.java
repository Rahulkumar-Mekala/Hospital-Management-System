package com.example.Hospital_Management_System.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Hospital_Management_System.entity.EmailOtp;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, UUID> {
	 void deleteByEmail(String email);
	Optional<EmailOtp> findTopByEmailOrderByExpiryTimeDesc(String email);
}
