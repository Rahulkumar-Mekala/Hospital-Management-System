package com.example.Hospital_Management_System.ServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Hospital_Management_System.ImageStorage.StorageService;
import com.example.Hospital_Management_System.ImageStorage.SupabaseStorageService;
import com.example.Hospital_Management_System.Repository.PatientIdentificationRepository;
import com.example.Hospital_Management_System.Repository.PatientRepository;
import com.example.Hospital_Management_System.Service.Patient_IdentificationService;
import com.example.Hospital_Management_System.entity.Patient;
import com.example.Hospital_Management_System.entity.Patient_Identification;
@Service
public class Patient_Identificationlmpl implements Patient_IdentificationService {
	
	private final PatientRepository patientRepository;
	private final PatientIdentificationRepository identificationRepository;
	private final StorageService storageService;
	 
	
	public Patient_Identificationlmpl(PatientRepository patientRepository,
			PatientIdentificationRepository identificationRepository, StorageService storageService) {
		super();
		this.patientRepository = patientRepository;
		this.identificationRepository = identificationRepository;
		this.storageService = storageService;
	}


	@Override
	public Patient_Identification saveIdentification(
	        String patientCode,
	        Patient_Identification identification,
	        MultipartFile frontImage,
	        MultipartFile backImage) throws IOException {
		
		
	    Patient patient = patientRepository.findByPatientCode(patientCode)
	            .orElseThrow(() -> new RuntimeException("Patient not found"));
	    
	    if (identificationRepository.findByPatientAndIdentificationType(patient,
	                    identification.getIdentificationType()) .isPresent()) {

	        throw new RuntimeException(
	                identification.getIdentificationType()
	                + " already exists for this patient.");
	    }	    identification.setPatient(patient);
	    
	    String identificationNumber = identification.getIdentificationNumber();

	    switch (identification.getIdentificationType()) {

	        case AADHAAR:
	            if (!identificationNumber.matches("^\\d{12}$")) {
	                throw new RuntimeException("Aadhaar number must contain exactly 12 digits.");
	            }
	            break;

	        case PAN:
	            if (!identificationNumber.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")) {
	                throw new RuntimeException("Invalid PAN number.");
	            }
	            break;

	        case PASSPORT:
	            if (!identificationNumber.matches("^[A-Z][0-9]{7}$")) {
	                throw new RuntimeException("Invalid Passport number.");
	            }
	            break;

	        case DRIVING_LICENSE:
	            if (identificationNumber.length() < 10) {
	                throw new RuntimeException("Invalid Driving License number.");
	            }
	            break;

	        default:
	            break;
	    }

	    if (frontImage != null && !frontImage.isEmpty()) {
	        String frontUrl = storageService.uploadImage(frontImage);
	        identification.setFrontImageUrl(frontUrl);
	    }

	    if (backImage != null && !backImage.isEmpty()) {
	        String backUrl = storageService.uploadImage(backImage);
	        identification.setBackImageUrl(backUrl);
	    }

	    return identificationRepository.save(identification);
	}          		   
	
	
	@Override
	public List<Patient_Identification> getAllIdentifications() {
		// TODO Auto-generated method stub
		return identificationRepository.findAll();
	}

	@Override
	public Patient_Identification getIdentificationById(UUID id) {
		// TODO Auto-generated method stub
		return identificationRepository.findById(id).orElseThrow(()-> new RuntimeException("Identification"));
	}

	@Override
	public List<Patient_Identification> getByPatientCode(String patientCode) {
		
		  Patient patient = patientRepository.findByPatientCode(patientCode).orElseThrow(()->new RuntimeException("Patient not found"));  
		
		   return patient.getIdentifications();
		}

	@Override
	public Patient_Identification updateIdentification(UUID id, Patient_Identification identification) {
		   
		 Patient_Identification existing = identificationRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Identification not found"));
		 
		 existing.setIdentificationType(identification.getIdentificationType());
		 existing.setIdentificationNumber(identification.getIdentificationNumber());
		 existing.setFrontImageUrl(identification.getFrontImageUrl());
		 existing.setBackImageUrl(identification.getBackImageUrl());
		 existing.setStatus(identification.getStatus());
		 return identificationRepository.save(existing);
	}

	@Override
	public void deleteIdentification(UUID id) {
		
		identificationRepository.deleteById(id);
		
	}


	
	




	

}
