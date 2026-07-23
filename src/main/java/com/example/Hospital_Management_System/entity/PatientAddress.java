package com.example.Hospital_Management_System.entity;

import java.util.UUID;

import com.example.Hospital_Management_System.Enum.AddressType;
import com.example.Hospital_Management_System.dto.PatientRecord;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "patient_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAddress {
	  @Id
	    @GeneratedValue(strategy = GenerationType.UUID)
	    private UUID id;

	   @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "patient_id", nullable = false)
	    private Patient patient;
	   
	   @Enumerated(EnumType.STRING)
	    @Column(name = "address_type")
	    private AddressType addressType;

	    @Column(name = "address_line1")
	    private String addressLine1;
	    @Column(name = "address_line2")
	    private String addressLine2;


	    @Column(name = "city")
	    private String city;

	    @Column(name = "state")
	    private String state;

	    @Column(name = "country")
	    private String country;

	    @Column(name = "postal_code")
	    private String postalCode;
	    
}
