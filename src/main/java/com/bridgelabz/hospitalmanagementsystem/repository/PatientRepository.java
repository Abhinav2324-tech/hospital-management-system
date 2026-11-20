package com.bridgelabz.hospitalmanagementsystem.repository;

import com.bridgelabz.hospitalmanagementsystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides database operations for patient records.
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {
}