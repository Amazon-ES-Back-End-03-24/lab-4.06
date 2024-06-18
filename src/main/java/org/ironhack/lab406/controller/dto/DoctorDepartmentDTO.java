package org.ironhack.lab406.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public class DoctorDepartmentDTO {
    @NotEmpty(message = "Department can't be empty or null.")
    private String department;

    public DoctorDepartmentDTO(String department) {
        this.department = department;
    }

    public DoctorDepartmentDTO() {
    }

    public @NotEmpty(message = "Department can't be empty or null.") String getDepartment() {
        return department;
    }

    public void setDepartment(@NotEmpty(message = "Department can't be empty or null.") String department) {
        this.department = department;
    }
}
