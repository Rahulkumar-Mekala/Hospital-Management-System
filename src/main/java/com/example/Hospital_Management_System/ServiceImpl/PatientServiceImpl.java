package com.example.Hospital_Management_System.ServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Hospital_Management_System.ImageStorage.SupabaseStorageService;
import com.example.Hospital_Management_System.Repository.PatientContactRepository;
import com.example.Hospital_Management_System.Repository.PatientRepository;
import com.example.Hospital_Management_System.Service.PatientService;

import com.example.Hospital_Management_System.entity.Patient;
import com.example.Hospital_Management_System.entity.PatientAddress;
import com.example.Hospital_Management_System.entity.PatientContact;
import com.example.Hospital_Management_System.entity.Patient_Identification;

@Service
public class PatientServiceImpl implements PatientService {
	private  final	PatientRepository patientRepository;
	private final SupabaseStorageService storageService;
	private final PatientContactRepository contactRepository;
	
	

	public PatientServiceImpl(PatientRepository patientRepository, SupabaseStorageService storageService,
			PatientContactRepository contactRepository) {
		super();
		this.patientRepository = patientRepository;
		this.storageService = storageService;
		this.contactRepository = contactRepository;
	}

	@Override
	public List<Patient> saveAllPatient(List<Patient> patient) {
		
		return patientRepository.saveAll(patient);
	}

	@Override
	public Patient savePatient(Patient patient,
            MultipartFile frontImage,
            MultipartFile backImage) throws IOException {
	   
		if (patientRepository.findByPatientCode(patient.getPatientCode()).isPresent()) {
	        throw new RuntimeException("Patient Code already exists");
	    }
		if (patient.getAddresses() != null) {
		    patient.getAddresses().forEach(address -> address.setPatient(patient));
		}
		if (patient.getContacts() != null) {
			for (PatientContact contact : patient.getContacts()) {
				contact.setPatient(patient);
	            if (contactRepository.existsByPrimarycontactnumber(contact.getPrimarycontactnumber())) {
	                throw new RuntimeException(
	                    " Primary Phone number already exists: " + contact.getPrimarycontactnumber());
	            }
	            for (PatientContact contact2 : patient.getContacts()) {
	                if (contactRepository.existsBySecondarycontactnumber(contact2.getSecondarycontactnumber())) {
	                    throw new RuntimeException(
	                        " Secondary Phone number already exists: " + contact.getSecondarycontactnumber());
	                }
	            }
	        }
		}
		if (patient.getIdentifications() != null) {

		    String frontUrl = storageService.uploadImage(frontImage);
		    String backUrl = storageService.uploadImage(backImage);

		    patient.getIdentifications().forEach(identification -> {identification.setPatient(patient);
		        identification.setFrontImageUrl(frontUrl);
		        identification.setBackImageUrl(backUrl);
		    });
		}

		if (patient.getInsurances() != null) {
		    patient.getInsurances().forEach(insurance -> insurance.setPatient(patient));
		}
		 patient.setPatientCode(generatePatientCode());
		
		return patientRepository.save(patient) ;
		
	}
System.out.println(patient.getInsurances());
System.out.println(patient.getInsurances().size());
	@Override
	public List<Patient> getAllPatients() {
		// TODO Auto-generated method stub
		return patientRepository.findAll();
	}

	@Override
	public Optional<Patient> getPatientById(UUID id) {
		
		return patientRepository.findById(id);
	}

	@Override
	public Patient updatePatient(UUID id, Patient patient) {
		
		  Patient existingPatient = patientRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Patient not found"));

	        
	        existingPatient.setFirstName(patient.getFirstName());
	        existingPatient.setMiddleName(patient.getMiddleName());
	        existingPatient.setLastName(patient.getLastName());
	        existingPatient.setGender(patient.getGender());
	        existingPatient.setDateOfBirth(patient.getDateOfBirth());
	        existingPatient.setBloodGroup(patient.getBloodGroup());
	        existingPatient.setMaritalStatus(patient.getMaritalStatus());
	        existingPatient.setNationality(patient.getNationality());
	        existingPatient.setOccupation(patient.getOccupation());
	        existingPatient.setEmail(patient.getEmail());
	        existingPatient.setStatus(patient.getStatus());
	        
	        if (patient.getAddresses() != null) {

	            existingPatient.getAddresses().clear();

	            for (PatientAddress address : patient.getAddresses()) {

	                address.setPatient(existingPatient);
	                existingPatient.getAddresses().add(address);
	            }
	        }

	        
	        return patientRepository.save(existingPatient);


	        
	}

	@Override
	public void deletePatient(UUID id) {
		patientRepository.deleteById(id);
		
	}

	@Override
	public Optional<Patient> findByPatientCode(String patientCode) {
		return patientRepository.findByPatientCode(patientCode);
	}

	@Override
	public Patient updatePatientforcode(String patientCode,
	                                    Patient patient,
	                                    MultipartFile frontImage,
	                                    MultipartFile backImage) throws IOException {

	    Patient existingPatient = patientRepository.findByPatientCode(patientCode)
	            .orElseThrow(() -> new RuntimeException("Patient not found"));

	    // Patient Details
	    existingPatient.setFirstName(patient.getFirstName());
	    existingPatient.setMiddleName(patient.getMiddleName());
	    existingPatient.setLastName(patient.getLastName());
	    existingPatient.setGender(patient.getGender());
	    existingPatient.setDateOfBirth(patient.getDateOfBirth());
	    existingPatient.setBloodGroup(patient.getBloodGroup());
	    existingPatient.setMaritalStatus(patient.getMaritalStatus());
	    existingPatient.setNationality(patient.getNationality());
	    existingPatient.setOccupation(patient.getOccupation());
	    existingPatient.setEmail(patient.getEmail());
	    existingPatient.setStatus(patient.getStatus());
	    existingPatient.getAddresses().clear();
	    // Address
	    existingPatient.getAddresses().clear();

	    if (patient.getAddresses() != null) {
	       
	        for (PatientAddress address : patient.getAddresses()) {
	            address.setPatient(existingPatient);
	            existingPatient.getAddresses().add(address);
	        }
	    }
	    existingPatient.getContacts().clear();
	    // Contact
	    if (patient.getContacts() != null) {
	        existingPatient.getContacts().clear();

	        if (patient.getContacts() != null) {
			for (PatientContact contact : patient.getContacts()) {
				contact.setPatient(patient);
	            if (contactRepository.existsByPrimarycontactnumber(contact.getPrimarycontactnumber())) {
	                throw new RuntimeException(
	                    " Primary Phone number already exists: " + contact.getPrimarycontactnumber());
	            }
	            for (PatientContact contact2 : patient.getContacts()) {
	                if (contactRepository.existsBySecondarycontactnumber(contact2.getSecondarycontactnumber())) {
	                    throw new RuntimeException(
	                        " Secondary Phone number already exists: " + contact.getSecondarycontactnumber());
	                }
	            }
	        }
		}
	    }
	   
	    if (patient.getIdentifications() != null && !patient.getIdentifications().isEmpty()) {
         
	    	   Patient_Identification newIdentification = patient.getIdentifications().get(0);

	           if (existingPatient.getIdentifications() != null &&
	                   !existingPatient.getIdentifications().isEmpty()) {
	        	   Patient_Identification existingIdentification =
	                       existingPatient.getIdentifications().get(0);

	        	   existingIdentification.setIdentificationType(newIdentification.getIdentificationType());
	               existingIdentification.setIdentificationNumber(newIdentification.getIdentificationNumber());
	               existingIdentification.setStatus(newIdentification.getStatus());

	               

	        if (frontImage != null && !frontImage.isEmpty()) {
	        	
	        	if (existingIdentification.getFrontImageUrl() != null &&
                        !existingIdentification.getFrontImageUrl().isBlank()) {
                  
	        		 storageService.deleteImage(existingIdentification.getFrontImageUrl());
	        	}
	        	 String frontUrl = storageService.uploadImage(frontImage);
	                existingIdentification.setFrontImageUrl(frontUrl);
	          
	        }
           
	        if (backImage != null && !backImage.isEmpty()) {
	        	if (existingIdentification.getBackImageUrl() != null &&
                        !existingIdentification.getBackImageUrl().isBlank()) {

                    storageService.deleteImage(existingIdentification.getBackImageUrl());
                }
	        	
	        	 String backUrl = storageService.uploadImage(backImage);
	                existingIdentification.setBackImageUrl(backUrl);
	        	
	        }
	     } else {
	    	 
	    	   newIdentification.setPatient(existingPatient);

	    	   if (frontImage != null && !frontImage.isEmpty()) {
	                String frontUrl = storageService.uploadImage(frontImage);
	                newIdentification.setFrontImageUrl(frontUrl);
	            }
	    	   
	    	   if (backImage != null && !backImage.isEmpty()) {
	                String backUrl = storageService.uploadImage(backImage);
	                newIdentification.setBackImageUrl(backUrl);
	            }


	           existingPatient.getIdentifications().add(newIdentification);
	        }
	    }

	    return patientRepository.save(existingPatient);
	}

	@Override
	public void deletePatient(String patientCode) throws IOException {
	   
		 Patient patient = patientRepository.findByPatientCode(patientCode)
		            .orElseThrow(() -> new RuntimeException("Patient not found with code: " + patientCode));
		 
		 if (patient.getIdentifications() != null) {
		        for (Patient_Identification identification : patient.getIdentifications()) {

		            if (identification.getFrontImageUrl() != null) {
		                storageService.deleteImage(identification.getFrontImageUrl());
		            }

		            if (identification.getBackImageUrl() != null) {
		                storageService.deleteImage(identification.getBackImageUrl());
		            }
		        }
		    }
		 patientRepository.delete(patient);
	
	}
	
	
	private String generatePatientCode() {

	    String lastCode = patientRepository.findLastPatientCode();

	    if (lastCode == null) {
	        return "PAT001";
	    }

	    int number = Integer.parseInt(lastCode.substring(3));

	    return String.format("PAT%03d", number + 1);
	}

	

}