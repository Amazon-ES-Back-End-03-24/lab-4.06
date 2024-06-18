package org.ironhack.lab406.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.ironhack.lab406.enums.EmployeeStatus;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    private Integer employeeId;

    @NotEmpty
    private String department;

    @NotEmpty
    private String name;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private EmployeeStatus status;

    @OneToMany(mappedBy = "admittedBy")
    @JsonIgnore
    private List<Patient> patients;

    public Doctor() {
    }

    public Doctor(Integer employeeId, String department, String name, EmployeeStatus status) {
        this.employeeId = employeeId;
        this.department = department;
        this.name = name;
        this.status = status;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(employeeId, doctor.employeeId) && Objects.equals(department, doctor.department) && Objects.equals(name, doctor.name) && status == doctor.status && Objects.equals(patients, doctor.patients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, department, name, status, patients);
    }
}
