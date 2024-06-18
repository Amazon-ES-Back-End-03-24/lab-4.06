package org.ironhack.lab406.repository;

import org.ironhack.lab406.enums.EmployeeStatus;
import org.ironhack.lab406.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findByStatus(EmployeeStatus status);

    List<Doctor> findByDepartment(String department);

    List<Doctor> findByDepartmentAndStatus(String department, EmployeeStatus status);
}
