package com.example.Hospital_Management_System.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record PatientRecord( 
UUID id,
String patientCode,
String firstName,
String middleName,
String lastName,
String gender,
LocalDate dateOfBirth,
String bloodGroup,
String maritalStatus,
String nationality,
String occupation,
String email,
String status,
List<PatientAddressResponse> addresses,
List<PatientContactRecord> contacts,
List<PatientIdentificationRecord> identifications,
List<Patient_Insurance>insurances
            ) {
public static PatientRecord from (com.example.Hospital_Management_System.entity.Patient a) {
	
	   return new PatientRecord(
	            a.getId(),
	            a.getPatientCode(),
	            a.getFirstName(), 
	            a.getMiddleName(),
	            a.getLastName(),
	            a.getGender(),
	            a.getDateOfBirth(),
	            a.getMaritalStatus(),
	            a.getNationality(),
	            a.getOccupation(),
	             a.getBloodGroup(),
	             a.getOccupation(),
	             a.getStatus(),
				 System.out.println(patient.getInsurances());
System.out.println(patient.getInsurances().size());
	             a.getAddresses() == null ? List.of() :
	                 a.getAddresses().stream().map(PatientAddressResponse::from).collect(Collectors.toList()),

	             a.getContacts() == null ? List.of() :
	                 a.getContacts().stream().map(PatientContactRecord::from).collect(Collectors.toList()),
	             a.getIdentifications() == null ? List.of() :
	                 a.getIdentifications().stream().map(PatientIdentificationRecord::from).collect(Collectors.toList()),

	                 a.getInsurances() == null ? List.of() :
		                 a.getInsurances().stream().map(Patient_Insurance::from).collect(Collectors.toList())

	        );
  }
}
