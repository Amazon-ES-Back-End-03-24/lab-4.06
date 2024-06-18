package org.ironhack.lab406.controller;

import jakarta.validation.Valid;
import org.ironhack.lab406.controller.dto.DoctorDTO;
import org.ironhack.lab406.controller.dto.DoctorDepartmentDTO;
import org.ironhack.lab406.controller.dto.DoctorStatusDTO;
import org.ironhack.lab406.enums.EmployeeStatus;
import org.ironhack.lab406.model.Doctor;
import org.ironhack.lab406.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Doctor> getDoctors(@RequestParam Optional<EmployeeStatus> status, @RequestParam Optional<String> department) {
        return doctorService.getDoctors(status, department);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Doctor getById(@PathVariable Integer id) {
        return doctorService.getDoctorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor store(@RequestBody @Valid DoctorDTO doctorDTO) {
        return doctorService.store(doctorDTO);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @RequestBody @Valid DoctorStatusDTO doctorStatusDTO) {
        doctorService.updateStatus(id, doctorStatusDTO);
    }

    @PatchMapping("/{id}/department")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDepartment(@PathVariable Integer id, @RequestBody @Valid DoctorDepartmentDTO doctorDepartmentDTO) {
        doctorService.updateDepartment(id, doctorDepartmentDTO);
    }

}
