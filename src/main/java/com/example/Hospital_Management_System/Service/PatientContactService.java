package com.example.Hospital_Management_System.Service;

import java.util.List;

import com.example.Hospital_Management_System.entity.PatientContact;

public interface PatientContactService {

	 PatientContact saveContact(String patientCode, PatientContact contact);
	 List<PatientContact> saveContacts(String patientCode,List<PatientContact> contacts);
	 
	    List<PatientContact> getAllContacts(String patientCode);

	    PatientContact getContact(String patientCode, String phoneNumber);

	    PatientContact updateContact(String patientCode,String phoneNumber, PatientContact contact);
	    
	    void deleteContact(String patientCode, String phoneNumber);
}
