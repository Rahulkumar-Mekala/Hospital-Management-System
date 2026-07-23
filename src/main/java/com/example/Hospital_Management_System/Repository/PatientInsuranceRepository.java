package com.example.Hospital_Management_System.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Hospital_Management_System.Enum.InsuranceStatus;
import com.example.Hospital_Management_System.entity.Patient;
import com.example.Hospital_Management_System.entity.PatientInsurance;
public interface PatientInsuranceRepository extends JpaRepository<PatientInsurance, UUID>{
	  List<PatientInsurance> findByPatient(Patient patient);

	    Optional<PatientInsurance> findByPolicyNumber(String policyNumber);

	    List<PatientInsurance> findByProviderName(String providerName);

	    List<PatientInsurance> findByStatus(InsuranceStatus status);

	    Optional<PatientInsurance> findByPatientPatientCode(String patientCode);

	    boolean existsByPolicyNumber(String policyNumber);
  
}