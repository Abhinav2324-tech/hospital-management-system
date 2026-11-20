package com.bridgelabz.hospitalmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Carries patient data between the API and the service layer.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer age;

    @NotBlank
    @Size(max = 255)
    private String gender;

    @NotBlank
    @Size(max = 255)
    private String disease;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Size(max = 255)
    private String phoneNumber;
}