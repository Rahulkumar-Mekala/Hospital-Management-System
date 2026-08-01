package com.example.Hospital_Management_System.Repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Hospital_Management_System.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	boolean existsByEmail(String Email);
	
	Optional<User> findByEmail(String email);
	  
}
