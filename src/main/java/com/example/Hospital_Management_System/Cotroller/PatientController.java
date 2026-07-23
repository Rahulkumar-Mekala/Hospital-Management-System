package com.example.Hospital_Management_System.Cotroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.Hospital_Management_System.ServiceImpl.PatientServiceImpl;
import com.example.Hospital_Management_System.dto.PatientRecord;
import com.example.Hospital_Management_System.entity.Patient;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientServiceImpl patientService;

    PatientController(PatientServiceImpl patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientRecord> getAllPatients() {
        return patientService.getAllPatients().stream()
                .map(PatientRecord::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Optional<PatientRecord> getPatientById(@PathVariable UUID id) {
        return patientService.getPatientById(id)
                .map(PatientRecord::from);
    }

    @GetMapping("/code/{patientCode}")
    public Optional<PatientRecord> getPatientBycode(@PathVariable String patientCode) {
        return patientService.findByPatientCode(patientCode)
                .map(PatientRecord::from);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> savePatient(
            @RequestPart("patient") Patient patient,
            @RequestPart("frontImage") MultipartFile frontImage,
            @RequestPart("backImage") MultipartFile backImage) throws IOException {
   try {
        Patient savedPatient = patientService.savePatient(patient, frontImage, backImage);
        return ResponseEntity.ok(PatientRecord.from(savedPatient));

    } catch (RuntimeException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(error);

    } catch (IOException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    }
       
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllPatients(@RequestBody List<Patient> patients) {
        try {
            List<Patient> savedPatients = patientService.saveAllPatient(patients);
            List<PatientRecord> patientRecords = savedPatients.stream()
                    .map(PatientRecord::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(patientRecords);
        } catch (RuntimeException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", HttpStatus.BAD_REQUEST.value());
            error.put("error", "Bad Request");
            error.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patient);
            return ResponseEntity.ok(PatientRecord.from(updatedPatient));
        } catch (RuntimeException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", HttpStatus.BAD_REQUEST.value());
            error.put("error", "Bad Request");
            error.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping( value = "/update/{patientCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePatientByCode(@PathVariable String patientCode,
    		 @RequestPart("patient") Patient patient,

             @RequestPart(value = "frontImage", required = false) MultipartFile frontImage,
             @RequestPart(value = "backImage", required = false) MultipartFile backImage) throws IOException{
    	
    	Patient updatedPatient = patientService.updatePatientforcode(patientCode,patient,  frontImage,  backImage);

    	 return ResponseEntity.ok(PatientRecord.from(updatedPatient));
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return "Patient deleted successfully";
    }

    @DeleteMapping("/delete/{patientCode}")
    public ResponseEntity<?> deletePatientByPatientCode(@PathVariable String patientCode) throws IOException {
    	patientService.deletePatient(patientCode);
        return ResponseEntity.ok("Patient deleted successfully");
    }
}