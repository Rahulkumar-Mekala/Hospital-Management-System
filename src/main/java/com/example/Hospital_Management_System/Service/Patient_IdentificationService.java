package com.example.Hospital_Management_System.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.example.Hospital_Management_System.entity.Patient_Identification;

@Repository
public interface Patient_IdentificationService {

	 Patient_Identification saveIdentification(
	            String patientCode,
	            Patient_Identification identification,
	            MultipartFile frontImage,
	            MultipartFile backImage) throws IOException;

	    List<Patient_Identification> getAllIdentifications();

	    Patient_Identification getIdentificationById(UUID id);

	    List<Patient_Identification> getByPatientCode(String patientCode);

	    Patient_Identification updateIdentification(UUID id, Patient_Identification identification);

	    void deleteIdentification(UUID id);
	   
}
