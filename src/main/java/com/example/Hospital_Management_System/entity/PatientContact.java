package com.example.Hospital_Management_System.entity;

import java.util.UUID;

import com.example.Hospital_Management_System.Enum.ContactType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient_contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientContact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type")
    private ContactType contactType;

    @Column(name = "primarycontactnumber_phone_number",unique = true ,nullable = false)
    private String primarycontactnumber;
    @Column(name = "secondarycontactnumber_phone_number",unique = true )
    private String secondarycontactnumber;

   
}