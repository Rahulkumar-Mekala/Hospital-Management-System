package com.example.Hospital_Management_System.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Hospital_Management_System.entity.PatientContact;

public interface PatientContactRepository  extends JpaRepository<PatientContact, UUID>{
	List<PatientContact> findByPatientPatientCode(String patientCode);
	boolean existsByPrimarycontactnumber(String primarycontactnumber);

	Optional<PatientContact> findByPatientPatientCodeAndPrimarycontactnumber(String patientCode,String primarycontactnumber);
	    boolean existsBySecondarycontactnumber(String secondarycontactnumber);

}
