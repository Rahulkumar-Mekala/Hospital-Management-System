package com.example.Hospital_Management_System.Cotroller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Hospital_Management_System.Enum.IdentificationType;
import com.example.Hospital_Management_System.ImageStorage.SupabaseStorageService;
import com.example.Hospital_Management_System.Service.Patient_IdentificationService;
import com.example.Hospital_Management_System.dto.PatientIdentificationRecord;
import com.example.Hospital_Management_System.entity.Patient_Identification;

@RestController
@RequestMapping("/api/patient-identifications")
public class PatientIdentificationController {

    private final Patient_IdentificationService identificationService;

    public PatientIdentificationController(Patient_IdentificationService identificationService) {
        this.identificationService = identificationService;
        
    }

   
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PatientIdentificationRecord> saveIdentification(

            @RequestParam String patientCode,
            @RequestParam IdentificationType identificationType,
            @RequestParam String identificationNumber,
            @RequestParam(required = false) String issuingAuthority,
            @RequestParam(required = false) String issueDate,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) String remarks,
            @RequestParam(required = false) MultipartFile frontImage,
            @RequestParam(required = false) MultipartFile backImage

    ) throws IOException {

        Patient_Identification identification = new Patient_Identification();

        identification.setIdentificationType(identificationType);
        identification.setIdentificationNumber(identificationNumber);
        Patient_Identification saved =
                identificationService.saveIdentification(
                        patientCode,
                        identification,
                        frontImage,
                        backImage);
        
         PatientIdentificationRecord record = PatientIdentificationRecord.from(saved);

        return new ResponseEntity<PatientIdentificationRecord>(record,HttpStatus.CREATED);
    }

   
    @GetMapping
    public ResponseEntity<List<PatientIdentificationRecord>> getAll() {
        return ResponseEntity.ok(
                identificationService.getAllIdentifications().stream().map(PatientIdentificationRecord::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientIdentificationRecord> getById(
            @PathVariable UUID id) {

  
                Patient_Identification identificationById = identificationService.getIdentificationById(id);
				return ResponseEntity.ok(PatientIdentificationRecord.from(identificationById));
                
    }

    @GetMapping("/patient/{patientCode}")
    public ResponseEntity<List<PatientIdentificationRecord>> getByPatientCode(
            @PathVariable String patientCode) {

        return ResponseEntity.ok(
                identificationService.getByPatientCode(patientCode).stream().map(PatientIdentificationRecord::from).collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientIdentificationRecord> update(
            @PathVariable UUID id,
            @RequestBody Patient_Identification identification) {

       
                Patient_Identification updateIdentification = identificationService.updateIdentification(id, identification);
                 return ResponseEntity.ok(PatientIdentificationRecord.from(updateIdentification));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable UUID id) {

        identificationService.deleteIdentification(id);

        return ResponseEntity.ok("Patient Identification deleted successfully.");
    }
}