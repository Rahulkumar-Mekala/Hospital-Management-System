package com.example.Hospital_Management_System.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.Hospital_Management_System.entity.Patient;

public interface PatientService {
	 List<Patient> saveAllPatient(List<Patient> patient);

	 Patient savePatient(Patient patient, MultipartFile frontImage, MultipartFile backImage) throws IOException;
	
	    List<Patient> getAllPatients();
	    Optional<Patient> getPatientById(UUID id);
	    Patient updatePatient(UUID id, Patient patient);
	    void deletePatient(UUID id);
	    Optional<Patient> findByPatientCode(String patientCode);
	    Patient updatePatientforcode(String patientCode, Patient patient,MultipartFile frontImage,MultipartFile backImage) throws IOException;
	    void deletePatient(String patientCode) throws IOException;
	
}
