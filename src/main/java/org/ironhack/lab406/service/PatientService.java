package org.ironhack.lab406.service;

import io.micrometer.common.util.StringUtils;
import org.ironhack.lab406.controller.dto.PatientDTO;
import org.ironhack.lab406.enums.EmployeeStatus;
import org.ironhack.lab406.model.Doctor;
import org.ironhack.lab406.model.Patient;
import org.ironhack.lab406.repository.DoctorRepository;
import org.ironhack.lab406.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Integer id) {
        return patientRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
    }

    public List<Patient> getByDateOfBirthBetween(Date start, Date end) {
        return patientRepository.findByDateOfBirthBetween(start, end);
    }

    public List<Patient> getByAdmittedByDepartment(String department) {
        return patientRepository.findByAdmittedByDepartment(department);
    }

    public List<Patient> getByAdmittedByStatus() {
        return patientRepository.findByAdmittedByStatus(EmployeeStatus.OFF);
    }

    public Patient store(PatientDTO patientDTO) {
        Optional<Doctor> doctor = doctorRepository.findById(patientDTO.getDoctorId());

        if (doctor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The employeeId of the doctor doesn't exist.");
        }

        List<Patient> patient = patientRepository.findAll();
        Patient newPatient = null;
        try {
            newPatient = new Patient(patientDTO.getName(), new SimpleDateFormat("yyyy-MM-dd").parse(patientDTO.getDateOfBirth()), doctor.get());
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date format.");
        }
        if (!patient.contains(newPatient)) {
            return patientRepository.save(newPatient);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This patient already exists in the system.");
        }
    }

    public void update(int id, PatientDTO patientDTO) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            if (StringUtils.isNotBlank(patientDTO.getName())) {
                patient.get().setName(patientDTO.getName());
            }
            if (patientDTO.getDateOfBirth() != null) {
                try {
                    patient.get().setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(patientDTO.getDateOfBirth()));
                } catch (ParseException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date format.");
                }
            }
            if (patientDTO.getDoctorId() != null) {
                Optional<Doctor> doctor = doctorRepository.findById(patientDTO.getDoctorId());
                if (doctor.isPresent()) {
                    patient.get().setAdmittedBy(doctor.get());
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The employeeId of the doctor doesn't exist.");
                }
            }
            patientRepository.save(patient.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The patientId doesn't exist.");
        }
    }
}
