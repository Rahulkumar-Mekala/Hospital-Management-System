package com.example.Hospital_Management_System.ServiceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.Hospital_Management_System.Enum.AddressType;
import com.example.Hospital_Management_System.Repository.PatientAddressRepository;
import com.example.Hospital_Management_System.Repository.PatientRepository;
import com.example.Hospital_Management_System.Service.Patient_AddressService;
import com.example.Hospital_Management_System.dto.PatientAddressResponse;
import com.example.Hospital_Management_System.entity.Patient;
import com.example.Hospital_Management_System.entity.PatientAddress;

@Service
public class Patient_AddressImpl implements Patient_AddressService {

    private final PatientRepository patientRepository;
    private final PatientAddressRepository addressRepository;
    

    public Patient_AddressImpl(PatientRepository patientRepository, PatientAddressRepository addressRepository) {
        this.patientRepository = patientRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public PatientAddressResponse saveAddress(String patientCode, PatientAddress address) {
        Patient patient = patientRepository.findByPatientCode(patientCode)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        address.setPatient(patient);
        PatientAddress saved = addressRepository.save(address);
        return PatientAddressResponse.from(saved);
    }

    @Override
    public List<PatientAddressResponse> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(PatientAddressResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientAddressResponse> getAddressesByPatient(String patientCode) {
        Patient patient = patientRepository.findByPatientCode(patientCode)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return addressRepository.findByPatient(patient)
                .stream()
                .map(PatientAddressResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public PatientAddressResponse getAddressById(UUID id) {
        PatientAddress address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return PatientAddressResponse.from(address);
    }

    @Override
    public void deleteAddress(UUID id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Address not found");
        }
        addressRepository.deleteById(id);
    }

    @Override
    public List<PatientAddressResponse> saveAddresses(String patientCode, List<PatientAddress> addresses) {
        Patient patient = patientRepository.findByPatientCode(patientCode)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        for (PatientAddress address : addresses) {
            address.setPatient(patient);
        }
        return addressRepository.saveAll(addresses)
                .stream()
                .map(PatientAddressResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public PatientAddressResponse updateAddress(String patientCode, AddressType addressType, PatientAddress address) {
        List<PatientAddress> addresses =
                addressRepository.findByPatientPatientCodeAndAddressType(patientCode, addressType);
        if (addresses.isEmpty()) {
            throw new RuntimeException("Address not found");
        }
        PatientAddress existing = addresses.get(0);

        existing.setAddressLine1(address.getAddressLine1());
        existing.setAddressLine2(address.getAddressLine2());
        existing.setCity(address.getCity());
        existing.setState(address.getState());
        existing.setCountry(address.getCountry());
        existing.setPostalCode(address.getPostalCode());

        PatientAddress saved = addressRepository.save(existing);
        return PatientAddressResponse.from(saved);
    }
}