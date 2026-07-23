package com.example.Hospital_Management_System.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Hospital_Management_System.Enum.IdentificationType;
import com.example.Hospital_Management_System.entity.Patient;
import com.example.Hospital_Management_System.entity.Patient_Identification;

public interface PatientIdentificationRepository extends JpaRepository<Patient_Identification, UUID> {

    List<Patient_Identification> findByPatientId(UUID patientId);

    List<Patient_Identification> findByPatient_PatientCode(String patientCode);

    Optional<Patient_Identification> findByIdentificationNumber(String identificationNumber);

    List<Patient_Identification> findByIdentificationType(IdentificationType identificationType);

    List<Patient_Identification> findByStatus(String status);

    Optional<Patient_Identification> findByPatientAndIdentificationType(
            Patient patient,
            IdentificationType identificationType);

    boolean existsByIdentificationNumber(String identificationNumber);

    void deleteByPatientId(UUID patientId);
}