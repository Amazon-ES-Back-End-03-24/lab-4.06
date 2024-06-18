package org.ironhack.lab406.controller.dto;

import jakarta.validation.constraints.NotNull;
import org.ironhack.lab406.enums.EmployeeStatus;

public class DoctorStatusDTO {
    @NotNull(message = "Status can't be null.")
    private EmployeeStatus status;

    public DoctorStatusDTO(EmployeeStatus status) {
        this.status = status;
    }

    public DoctorStatusDTO() {
    }

    public @NotNull(message = "Status can't be null.") EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Status can't be null.") EmployeeStatus employeeStatus) {
        this.status = employeeStatus;
    }
}
