package com.example.Hospital_Management_System.dto;

import java.util.UUID;

import com.example.Hospital_Management_System.Enum.ContactType;
import com.example.Hospital_Management_System.entity.PatientContact;

public record PatientContactRecord(
    UUID id,
    UUID patientId,
    ContactType contactType,
    String primarycontactphoneNumber,
    String secondarycontactphoneNumber
    
) {

    public static PatientContactRecord from(PatientContact patientContact) {
        return new PatientContactRecord(
            patientContact.getId(),
            patientContact.getPatient()!= null ? patientContact.getPatient().getId() : null,
            patientContact.getContactType(),
            patientContact.getPrimarycontactnumber(),
            patientContact.getSecondarycontactnumber()
        );
    }
}