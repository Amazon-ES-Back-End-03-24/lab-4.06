package org.ironhack.lab406.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.ironhack.lab406.enums.EmployeeStatus;

public class DoctorDTO {
    @NotNull(message = "EmployeeId can't be null.")
    private Integer employeeId;
    @NotEmpty(message = "Name can't be empty or null.")
    private String name;
    @NotEmpty(message = "Department can't be empty or null.")
    private String department;
    @NotNull(message = "Status can't be null.")
    private EmployeeStatus status;

    public DoctorDTO() {
    }

    public DoctorDTO(Integer employeeId, String name, String department, EmployeeStatus employeeStatus) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.status = employeeStatus;
    }

    public @NotNull(message = "EmployeeId can't be null.") Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(@NotNull(message = "EmployeeId can't be null.") Integer employeeId) {
        this.employeeId = employeeId;
    }

    public @NotEmpty(message = "Name can't be empty or null.") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Name can't be empty or null.") String name) {
        this.name = name;
    }

    public @NotEmpty(message = "Department can't be empty or null.") String getDepartment() {
        return department;
    }

    public void setDepartment(@NotEmpty(message = "Department can't be empty or null.") String department) {
        this.department = department;
    }

    public @NotNull(message = "Status can't be null.") EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Status can't be null.") EmployeeStatus status) {
        this.status = status;
    }
}
