package com.bridgelabz.hospitalmanagementsystem.controller;

import com.bridgelabz.hospitalmanagementsystem.dto.PatientDto;
import com.bridgelabz.hospitalmanagementsystem.service.PatientService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Exposes the patient CRUD endpoints protected by JWT authentication.
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientDto> addPatient(
            @Valid @RequestBody PatientDto request) {
        PatientDto patient = patientService.createPatient(request);
        URI location = URI.create("/api/patients/" + patient.getId());
        return ResponseEntity.created(location).body(patient);
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(
            @PathVariable("id") Long id,
            @Valid @RequestBody PatientDto request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}