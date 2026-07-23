package com.example.Hospital_Management_System.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.example.Hospital_Management_System.Enum.InsuranceStatus;

public record Patient_Insurance(
		UUID insuranceId,
	    UUID patientId,
	    String patientCode,
	    String providerName,
	    String policyNumber,
	    BigDecimal coverageAmount,
	    LocalDate startDate,
	    LocalDate endDate,
	    BigDecimal premiumAmount,
	    InsuranceStatus status,
	    BigDecimal claimLimit
		) {

	public static Patient_Insurance from(com.example.Hospital_Management_System.entity.PatientInsurance insurance) {
		
		return new Patient_Insurance(
				insurance.getInsuranceId(),
				insurance.getPatient() != null ? insurance.getPatient().getId() : null,
				insurance.getPatient() != null ? insurance.getPatient().getPatientCode() : null,
				insurance.getProviderName(),
				insurance.getPolicyNumber(),
				insurance.getCoverageAmount(),
				insurance.getStartDate(),
				insurance.getEndDate(),
				insurance.getPremiumAmount(),
				insurance.getStatus(),
				insurance.getClaimLimit()
				
				
						
					);
	}
}
