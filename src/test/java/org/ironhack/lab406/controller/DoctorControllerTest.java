package org.ironhack.lab406.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.lab406.controller.dto.DoctorDTO;
import org.ironhack.lab406.controller.dto.DoctorDepartmentDTO;
import org.ironhack.lab406.controller.dto.DoctorStatusDTO;
import org.ironhack.lab406.enums.EmployeeStatus;
import org.ironhack.lab406.model.Doctor;
import org.ironhack.lab406.model.Patient;
import org.ironhack.lab406.repository.DoctorRepository;
import org.ironhack.lab406.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DoctorControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Doctor> doctors;
    private List<Patient> patients;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        doctors = doctorRepository.saveAll(
                List.of(
                        new Doctor(356712, "cardiology", "Alonso Flores", EmployeeStatus.ON_CALL),
                        new Doctor(564134, "immunology", "Sam Ortega", EmployeeStatus.ON),
                        new Doctor(761527, "cardiology", "German Ruiz", EmployeeStatus.OFF),
                        new Doctor(166552, "pulmonary", "Maria Lin", EmployeeStatus.ON),
                        new Doctor(156545, "orthopaedic", "Paolo Rodriguez", EmployeeStatus.ON_CALL),
                        new Doctor(172456, "psychiatric", "John Paul Armes", EmployeeStatus.OFF)
                )
        );

        patients = patientRepository.saveAll(
                List.of(
                        new Patient("Jaime Jordan", parseDate("1984-03-02"), doctors.get(1)),
                        new Patient("Marian Garcia", parseDate("1972-01-12"), doctors.get(1)),
                        new Patient("Julia Dusterdieck", parseDate("1954-06-11"), doctors.get(0)),
                        new Patient("Steve McDuck", parseDate("1931-11-10"), doctors.get(2)),
                        new Patient("Marian Garcia", parseDate("1999-02-15"), doctors.get(5))
                )
        );
    }

    @AfterEach
    void tearDown() {
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
    }

    @Test
    void getDoctors_withDoctors_listOfDoctors() throws Exception {
        MvcResult result = mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Alonso Flores"));
        assertTrue(result.getResponse().getContentAsString().contains("German Ruiz"));
        assertTrue(result.getResponse().getContentAsString().contains("Paolo Rodriguez"));
    }

    @Test
    void getDoctors_withoutDoctors_emptyList() throws Exception {
        patientRepository.deleteAll();
        doctorRepository.deleteAll();

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getById_correctId_doctor() throws Exception {
        MvcResult result = mockMvc.perform(get("/doctors/{id}", doctors.get(0).getEmployeeId()))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Alonso Flores"));
    }

    @Test
    void getById_incorrectId_notFound() throws Exception {
        mockMvc.perform(get("/doctors/{id}", 0))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void store_correctDoctor_newDoctor() throws Exception {
        DoctorDTO doctorDTO = new DoctorDTO(123456, "Pepe", "immunology", EmployeeStatus.OFF);
        String body = objectMapper.writeValueAsString(doctorDTO);

        MvcResult result = mockMvc.perform(post("/doctors")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Pepe"));
    }

    @Test
    void store_incorrectIdAndStatus_badRequest() throws Exception {
        DoctorDTO doctorDTO = new DoctorDTO(null, "Pepe", "immunology", null);
        String body = objectMapper.writeValueAsString(doctorDTO);

        mockMvc.perform(post("/doctors")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void store_incorrectNameAndDepartment_badRequest() throws Exception {
        DoctorDTO doctorDTO = new DoctorDTO(123456, "  ", "", EmployeeStatus.OFF);
        String body = objectMapper.writeValueAsString(doctorDTO);

        mockMvc.perform(post("/doctors")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStatus_correctStatus_noContent() throws Exception {
        DoctorStatusDTO doctorStatusDTO = new DoctorStatusDTO(EmployeeStatus.OFF);
        String body = objectMapper.writeValueAsString(doctorStatusDTO);

        mockMvc.perform(patch("/doctors/{id}/status", doctors.get(3).getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(doctorRepository.findById(doctors.get(3).getEmployeeId()).get().getStatus(), EmployeeStatus.OFF);


        doctorStatusDTO.setStatus(EmployeeStatus.ON);
        body = objectMapper.writeValueAsString(doctorStatusDTO);

        mockMvc.perform(patch("/doctors/{id}/status", doctors.get(2).getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(doctorRepository.findById(doctors.get(2).getEmployeeId()).get().getStatus(), EmployeeStatus.ON);
    }

    @Test
    void updateStatus_incorrectStatus_badRequest() throws Exception {
        DoctorStatusDTO doctorStatusDTO = new DoctorStatusDTO(null);
        String body = objectMapper.writeValueAsString(doctorStatusDTO);

        mockMvc.perform(patch("/doctors/{id}/status", doctors.get(4).getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void updateDepartment_correctDepartment_noContent() throws Exception {
        DoctorDepartmentDTO doctorDepartmentDTO = new DoctorDepartmentDTO("orthopaedic");
        String body = objectMapper.writeValueAsString(doctorDepartmentDTO);

        mockMvc.perform(patch("/doctors/{id}/department", doctors.get(4).getEmployeeId())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(doctorRepository.findById(doctors.get(4).getEmployeeId()).get().getDepartment(), "orthopaedic");

        doctorDepartmentDTO.setDepartment("pulmonary");
        body = objectMapper.writeValueAsString(doctorDepartmentDTO);

        mockMvc.perform(patch("/doctors/{id}/department", doctors.get(1).getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(doctorRepository.findById(doctors.get(1).getEmployeeId()).get().getDepartment(), "pulmonary");
    }

    @Test
    void updateDepartment_incorrectDepartment_noContent() throws Exception {
        DoctorDepartmentDTO doctorDepartmentDTO = new DoctorDepartmentDTO("");
        String body = objectMapper.writeValueAsString(doctorDepartmentDTO);

        mockMvc.perform(patch("/doctors/{id}/department", doctors.get(0).getEmployeeId()).content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}