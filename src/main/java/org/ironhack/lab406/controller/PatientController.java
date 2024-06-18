package org.ironhack.lab406.controller;

import jakarta.validation.Valid;
import org.ironhack.lab406.controller.dto.PatientDTO;
import org.ironhack.lab406.model.Patient;
import org.ironhack.lab406.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> findAll() {
        return patientService.getPatients();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Patient findById(@PathVariable Integer id) {
        return patientService.getPatientById(id);
    }

    @GetMapping("/between-date-of-birth")
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> findBetweenDateOfBirth(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {
        return patientService.getByDateOfBirthBetween(start, end);
    }

    @GetMapping("/doctor-department/{department}")
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> findByDoctorDepartment(@PathVariable String department) {
        return patientService.getByAdmittedByDepartment(department);
    }

    @GetMapping("/off-doctor")
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> findByDoctorStatus() {
        return patientService.getByAdmittedByStatus();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient store(@RequestBody @Valid PatientDTO patientDTO) throws ParseException {
        return patientService.store(patientDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody @Valid PatientDTO patientDTO) {
        patientService.update(id, patientDTO);
    }
}
