package com.example.Hospital_Management_System.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Hospital_Management_System.Repository.PatientContactRepository;
import com.example.Hospital_Management_System.Repository.PatientRepository;
import com.example.Hospital_Management_System.Service.PatientContactService;
import com.example.Hospital_Management_System.entity.Patient;
import com.example.Hospital_Management_System.entity.PatientContact;
@Service
public class PatientContactServiceImpl implements PatientContactService {
   
	private PatientRepository patientRepository;
	private PatientContactRepository patientContactRepository;
	
	
	public PatientContactServiceImpl(PatientRepository patientRepository,
			PatientContactRepository patientContactRepository) {
		
		this.patientRepository = patientRepository;
		this.patientContactRepository = patientContactRepository;
	}

	@Override
	public PatientContact saveContact(String patientCode, PatientContact contact) {
	Patient patient = patientRepository.findByPatientCode(patientCode)
	 	.orElseThrow(()-> new RuntimeException("Patient not found"));
	
	 if (patientContactRepository.existsByPrimarycontactnumber(contact.getPrimarycontactnumber())) {
         throw new RuntimeException(" primary Phone Number already exists");
     }
	 if (patientContactRepository.existsBySecondarycontactnumber(contact.getSecondarycontactnumber())) {
         throw new RuntimeException(" secondary Phone Number already exists");
     }
	  contact.setPatient(patient);
	 return  patientContactRepository.save(contact);
	}

	@Override
	public List<PatientContact> getAllContacts(String patientCode) {
		

        return patientContactRepository.findByPatientPatientCode(patientCode);
	}

	@Override
	public PatientContact getContact(String patientCode, String phoneNumber) {
		return patientContactRepository .findByPatientPatientCodeAndPrimarycontactnumber( patientCode,phoneNumber)
	                .orElseThrow(() ->new RuntimeException("Contact not found"));
		
	}

	@Override
	public PatientContact updateContact(String patientCode, String phoneNumber, PatientContact contact) {
		
		  PatientContact existing = patientContactRepository.findByPatientPatientCodeAndPrimarycontactnumber(patientCode, phoneNumber)
	                .orElseThrow(() ->  new RuntimeException("Contact not found"));
		  
	        existing.setContactType(contact.getContactType());
	        existing.setPrimarycontactnumber(contact.getPrimarycontactnumber());
	        existing.setSecondarycontactnumber(contact.getSecondarycontactnumber());
	        return patientContactRepository.save(existing);
	}

	@Override
	public void deleteContact(String patientCode, String phoneNumber) {
		  PatientContact contact = patientContactRepository .findByPatientPatientCodeAndPrimarycontactnumber(  patientCode, phoneNumber)
	                .orElseThrow(() -> new RuntimeException("Contact not found"));

		  patientContactRepository.delete(contact);
		
	}

	@Override
	public List<PatientContact> saveContacts(String patientCode, List<PatientContact> contacts) {
		 Patient patient = patientRepository.findByPatientCode(patientCode).orElseThrow(() -> new RuntimeException("Patient not found"));
		    for (PatientContact contact : contacts) {
		        if (patientContactRepository.existsByPrimarycontactnumber(contact.getPrimarycontactnumber())) {
		            throw new RuntimeException(
		                    " primary Phone Number already exists : " + contact.getPrimarycontactnumber());
		        }
		        if (patientContactRepository.existsBySecondarycontactnumber(contact.getSecondarycontactnumber())) {
		            throw new RuntimeException(
		                    " Secondary Phone Number already exists : " + contact.getSecondarycontactnumber());
		        }
         contact.setPatient(patient);
		    }

		    return patientContactRepository.saveAll(contacts);
	}

	

}
