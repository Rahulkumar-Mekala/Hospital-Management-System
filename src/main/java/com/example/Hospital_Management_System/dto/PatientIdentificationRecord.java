package com.example.Hospital_Management_System.dto;

import java.util.UUID;

import com.example.Hospital_Management_System.Enum.IdentificationType;
import com.example.Hospital_Management_System.entity.Patient_Identification;

public record PatientIdentificationRecord(
    UUID id,
    UUID patientId,
    IdentificationType identificationType,
    String identificationNumber,
    String frontImageUrl,
    String backImageUrl
 
) {

    public static PatientIdentificationRecord from(Patient_Identification identification) {
        return new PatientIdentificationRecord(
            identification.getId(),
            identification.getPatient() != null ? identification.getPatient().getId() : null,
            identification.getIdentificationType(),
            identification.getIdentificationNumber(),
            identification.getFrontImageUrl(),
            identification.getBackImageUrl()
            
        );
    }
}