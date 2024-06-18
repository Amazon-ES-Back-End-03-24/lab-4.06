package org.ironhack.lab406.controller.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;

public class PatientDTO {
    private String name;

    @Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "Date format must be YYYY-MM-DD")
    private String dateOfBirth;

    @Digits(integer = 10, fraction = 0, message = "Maximum number of digits for employeeId is 10, no decimals.")
    private Integer doctorId;

    public PatientDTO() {
    }

    public PatientDTO(String name, String dateOfBirth, Integer doctorId) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "Date format must be YYYY-MM-DD") String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "Date format must be YYYY-MM-DD") String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
}
