package com.example.Hospital_Management_System.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Hospital_Management_System.entity.Patient;


public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByEmail(String email);
    boolean existsByPatientCode(String patientCode);
	Optional<Patient> findByPatientCode(String patientCode);
     @Query(value = """
            SELECT patient_code
            FROM patient
            ORDER BY CAST(SUBSTRING(patient_code, 4) AS INTEGER) DESC
            LIMIT 1
            """, nativeQuery = true)
    String findLastPatientCode();
    

	}


