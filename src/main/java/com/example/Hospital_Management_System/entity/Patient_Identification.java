package com.example.Hospital_Management_System.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.Hospital_Management_System.Enum.IdentificationType;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "patient_identification")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient_Identification {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.UUID)
	    @Column(nullable = false, updatable = false)
	    private UUID id;
	  @OneToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "patient_id", nullable = false)
	  private Patient patient;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "identification_type", nullable = false)
	    private IdentificationType identificationType;

	    @Column(name = "identification_number", nullable = false, length = 100)
	    private String identificationNumber;

	    @Column(name = "front_image_url", length = 500)
	    private String frontImageUrl;

	    @Column(name = "back_image_url", length = 500)
	    private String backImageUrl;

	    @Column(name = "status", length = 20)
	    private String status;
	   
	    @Column(name = "created_at", updatable = false)
	    private LocalDateTime createdAt;

	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;

	    @PrePersist
	    public void prePersist() {
	        createdAt = LocalDateTime.now();
	        updatedAt = LocalDateTime.now();

	        if (status == null) {
	            status = "ACTIVE";
	        }
	    }

	    @PreUpdate
	    public void preUpdate() {
	        updatedAt = LocalDateTime.now();
	    }
}
