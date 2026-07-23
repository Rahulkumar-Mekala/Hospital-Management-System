package com.example.Hospital_Management_System.Cotroller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
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
    public PatientRecord savePatient(
            @RequestPart("patient") Patient patient,
            @RequestPart("frontImage") MultipartFile frontImage,
            @RequestPart("backImage") MultipartFile backImage) throws IOException {

        return PatientRecord.from(
                patientService.savePatient(patient, frontImage, backImage));
    }
       
    @PostMapping("/bulk")
    public List<PatientRecord> saveAllPatients(@RequestBody List<Patient> patients) {
        return patientService.saveAllPatient(patients)
                .stream()
                .map(PatientRecord::from)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public PatientRecord updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {
        return PatientRecord.from(patientService.updatePatient(id, patient));
    }

    @PutMapping( value = "/update/{patientCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PatientRecord updatePatientByCode(@PathVariable String patientCode,
    		 @RequestPart("patient") Patient patient,

             @RequestPart(value = "frontImage", required = false) MultipartFile frontImage,
             @RequestPart(value = "backImage", required = false) MultipartFile backImage) throws IOException{
    	
    	Patient updatedPatient = patientService.updatePatientforcode(patientCode,patient,  frontImage,  backImage);

    	 return PatientRecord.from(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return "Patient deleted successfully";
    }

    @DeleteMapping("/delete/{patientCode}")
    public String deletePatientByPatientCode(@PathVariable String patientCode) throws IOException {
    	patientService.deletePatient(patientCode);
        return "Patient deleted successfully";
    }
}