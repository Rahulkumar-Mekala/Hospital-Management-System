package com.example.Hospital_Management_System.entity;



import java.time.LocalDateTime;
import java.util.UUID;

import com.example.Hospital_Management_System.Enum.Qualification;
import com.example.Hospital_Management_System.Enum.Role;
import com.example.Hospital_Management_System.Enum.Specialization;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    
    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
   
    @Enumerated(EnumType.STRING)
    private Qualification qualification;
    
    @Enumerated(EnumType.STRING)
    private Specialization specialization; 
    
    @Column(name = "license_number", unique = true)
    private String licenseNumber;    

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE";

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}