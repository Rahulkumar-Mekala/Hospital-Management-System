package com.example.Hospital_Management_System.Service;

import java.util.List;
import java.util.UUID;

import com.example.Hospital_Management_System.Enum.AddressType;
import com.example.Hospital_Management_System.dto.PatientAddressResponse;
import com.example.Hospital_Management_System.entity.PatientAddress;

public interface Patient_AddressService {
    PatientAddressResponse saveAddress(String patientCode, PatientAddress address);
    List<PatientAddressResponse> getAllAddresses();
    List<PatientAddressResponse> getAddressesByPatient(String patientCode);
    PatientAddressResponse getAddressById(UUID id);
    void deleteAddress(UUID id);
    List<PatientAddressResponse> saveAddresses(String patientCode, List<PatientAddress> addresses);
    PatientAddressResponse updateAddress(String patientCode, AddressType addressType, PatientAddress address);
}