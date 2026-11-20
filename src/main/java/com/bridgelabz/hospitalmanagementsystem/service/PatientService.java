package com.bridgelabz.hospitalmanagementsystem.service;

import com.bridgelabz.hospitalmanagementsystem.dto.PatientDto;
import com.bridgelabz.hospitalmanagementsystem.entity.Patient;
import com.bridgelabz.hospitalmanagementsystem.repository.PatientRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Performs patient CRUD using records stored in MySQL.
 */
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientDto createPatient(PatientDto request) {
        Patient patient = new Patient();
        copyPatientDetails(request, patient);
        return toDto(patientRepository.save(patient));
    }

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public PatientDto getPatientById(Long id) {
        return toDto(findPatient(id));
    }

    @Transactional
    public PatientDto updatePatient(Long id, PatientDto request) {
        Patient patient = findPatient(id);
        copyPatientDetails(request, patient);
        return toDto(patientRepository.save(patient));
    }

    @Transactional
    public void deletePatient(Long id) {
        patientRepository.delete(findPatient(id));
    }

    private Patient findPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Patient not found with ID " + id));
    }

    private void copyPatientDetails(PatientDto request, Patient patient) {
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setDisease(request.getDisease());
        patient.setAddress(request.getAddress());
        patient.setPhoneNumber(request.getPhoneNumber());
    }

    private PatientDto toDto(Patient patient) {
        return new PatientDto(
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getDisease(),
                patient.getAddress(),
                patient.getPhoneNumber()
        );
    }
}