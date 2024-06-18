package org.ironhack.lab406.service;

import org.ironhack.lab406.controller.dto.DoctorDTO;
import org.ironhack.lab406.controller.dto.DoctorDepartmentDTO;
import org.ironhack.lab406.controller.dto.DoctorStatusDTO;
import org.ironhack.lab406.enums.EmployeeStatus;
import org.ironhack.lab406.model.Doctor;
import org.ironhack.lab406.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    public DoctorRepository doctorRepository;

    public List<Doctor> getDoctors(Optional<EmployeeStatus> status, Optional<String> department) {
        if (status.isPresent() && department.isPresent()) {
            return doctorRepository.findByDepartmentAndStatus(department.get(), status.get());
        } else if (status.isPresent()) {
            return doctorRepository.findByStatus(status.get());
        } else if (department.isPresent()) {
            return doctorRepository.findByDepartment(department.get());
        } else {
            return doctorRepository.findAll();
        }
    }

    public Doctor getDoctorById(Integer id) {
        return doctorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
    }

    public Doctor store(DoctorDTO doctorDTO) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorDTO.getEmployeeId());
        if (doctor.isEmpty()) {
            try {
                Doctor newDoctor = new Doctor(doctorDTO.getEmployeeId(), doctorDTO.getDepartment(), doctorDTO.getName(), doctorDTO.getStatus());
                return doctorRepository.save(newDoctor);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department and / or status values not valid.");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The employeeId already exists in the system.");
        }
    }

    public void updateStatus(Integer id, DoctorStatusDTO statusDTO) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            try {
                doctor.get().setStatus(statusDTO.getStatus());
                doctorRepository.save(doctor.get());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status value not valid.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The employeeId doesn't exist.");
        }
    }

    public void updateDepartment(Integer id, DoctorDepartmentDTO doctorDepartmentDTO) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            try {
                doctor.get().setDepartment(doctorDepartmentDTO.getDepartment());
                doctorRepository.save(doctor.get());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department value not valid.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The employeeId doesn't exist.");
        }
    }
}
