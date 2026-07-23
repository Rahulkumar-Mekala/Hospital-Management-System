package com.example.Hospital_Management_System.Cotroller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hospital_Management_System.Service.PatientContactService;
import com.example.Hospital_Management_System.dto.PatientContactRecord;
import com.example.Hospital_Management_System.entity.PatientContact;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patients/{patientCode}/contacts")
@RequiredArgsConstructor
public class PatientContactController {

	 private final PatientContactService contactService;

	    @PostMapping
	    public ResponseEntity<PatientContactRecord> saveContact(@PathVariable String patientCode, @RequestBody PatientContact contact) {
    PatientContact patientContact = contactService.saveContact(patientCode, contact);
    return ResponseEntity.ok(PatientContactRecord.from(patientContact));
	    }
	    
	    @PostMapping("/All")
	    public ResponseEntity<List<PatientContact>> saveContacts( @PathVariable String patientCode, @RequestBody List<PatientContact> contacts) {

	        return ResponseEntity.ok( contactService.saveContacts(patientCode, contacts));
	    }
	    @GetMapping
	    public ResponseEntity<List<PatientContactRecord>> getAllContacts( @PathVariable String patientCode) {
     List<PatientContactRecord> allContacts = contactService.getAllContacts(patientCode).stream().map(PatientContactRecord::from).collect(Collectors.toList());
     return ResponseEntity.ok(allContacts);
	    }

	    @GetMapping("/{phoneNumber}")
	    public ResponseEntity<PatientContactRecord> getContact(@PathVariable String patientCode,  @PathVariable String phoneNumber) {
	        PatientContact contact = contactService.getContact(patientCode, phoneNumber);
	        return ResponseEntity.ok(PatientContactRecord.from(contact));
	    }

	    @PutMapping("/{phoneNumber}")
	    public ResponseEntity<PatientContactRecord> updateContact(@PathVariable String patientCode,@PathVariable String phoneNumber,@RequestBody PatientContact contact) {
	        PatientContact updateContact = contactService.updateContact(patientCode,phoneNumber,contact);
			return ResponseEntity.ok(PatientContactRecord.from(updateContact));
	        
	    }

	    @DeleteMapping("/{phoneNumber}")
	    public ResponseEntity<String> deleteContact( @PathVariable String patientCode, @PathVariable String phoneNumber) {

	        contactService.deleteContact(patientCode, phoneNumber);

	        return ResponseEntity.ok("Contact deleted successfully");
	    }

}
