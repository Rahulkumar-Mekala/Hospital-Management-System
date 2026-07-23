package com.example.Hospital_Management_System.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Hospital_Management_System.Enum.AddressType;
import com.example.Hospital_Management_System.entity.Patient;
import com.example.Hospital_Management_System.entity.PatientAddress;

public interface PatientAddressRepository extends JpaRepository<PatientAddress, UUID> {
    
    List<PatientAddress> findByPatient(Patient patient);
    List<PatientAddress> findByPatientPatientCodeAndAddressType(String patientCode, AddressType addressType);
  }
