package com.example.Hospital_Management_System.Cotroller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Hospital_Management_System.Enum.AddressType;
import com.example.Hospital_Management_System.Service.Patient_AddressService;
import com.example.Hospital_Management_System.dto.PatientAddressResponse;
import com.example.Hospital_Management_System.entity.PatientAddress;

@RestController
@RequestMapping("/api/patient-address")
@CrossOrigin("*")
public class Patient_AddressController {

    private final Patient_AddressService patientAddressService;

    public Patient_AddressController(Patient_AddressService patientAddressService) {
        this.patientAddressService = patientAddressService;
    }

    @PostMapping("/{patientCode}")
    public ResponseEntity<PatientAddressResponse> saveAddress(
            @PathVariable String patientCode, @RequestBody PatientAddress address) {
        return ResponseEntity.ok(patientAddressService.saveAddress(patientCode, address));
    }

    @PostMapping("/multi/{patientCode}")
    public ResponseEntity<List<PatientAddressResponse>> saveAddresses(
            @PathVariable String patientCode, @RequestBody List<PatientAddress> addresses) {
        return ResponseEntity.ok(patientAddressService.saveAddresses(patientCode, addresses));
    }

    @GetMapping
    public ResponseEntity<List<PatientAddressResponse>> getAllAddresses() {
        return ResponseEntity.ok(patientAddressService.getAllAddresses());
    }

    @GetMapping("/patient/{patientCode}")
    public ResponseEntity<List<PatientAddressResponse>> getAddressesByPatient(
            @PathVariable String patientCode) {
        return ResponseEntity.ok(patientAddressService.getAddressesByPatient(patientCode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientAddressResponse> getAddressById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientAddressService.getAddressById(id));
    }

    @PutMapping("/{patientCode}/{addressType}")
    public ResponseEntity<PatientAddressResponse> updateAddress(
            @PathVariable String patientCode,
            @PathVariable AddressType addressType,
            @RequestBody PatientAddress address) {
        return ResponseEntity.ok(patientAddressService.updateAddress(patientCode, addressType, address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable UUID id) {
        patientAddressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}