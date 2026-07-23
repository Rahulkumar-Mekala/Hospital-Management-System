package com.example.Hospital_Management_System.dto;

import com.example.Hospital_Management_System.Enum.AddressType;
import java.util.UUID;

public record PatientAddressResponse(
    UUID id,
    UUID patientId,
    String patientCode,
    AddressType addressType,
    String addressLine1,
    String addressLine2,
    String city,
    String state,
    String country,
    String postalCode
) {
    public static PatientAddressResponse from(com.example.Hospital_Management_System.entity.PatientAddress a) {
        return new PatientAddressResponse(
            a.getId(),
            a.getPatient() != null ? a.getPatient().getId() : null,
            a.getPatient() != null ? a.getPatient().getPatientCode() : null,
            a.getAddressType(),
            a.getAddressLine1(),
            a.getAddressLine2(),
            a.getCity(),
            a.getState(),
            a.getCountry(),
            a.getPostalCode()
        );
    }
}