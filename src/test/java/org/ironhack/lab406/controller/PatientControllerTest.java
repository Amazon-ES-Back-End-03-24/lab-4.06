package org.ironhack.lab406.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.lab406.controller.dto.PatientDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PatientControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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
    void findAll_withPatients_listOfPatients() throws Exception {
        MvcResult result = mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Jaime Jordan"));
        assertTrue(result.getResponse().getContentAsString().contains("Julia Dusterdieck"));
        assertTrue(result.getResponse().getContentAsString().contains("Marian Garcia"));
    }

    @Test
    void findAll_withoutPatients_emptyList() throws Exception {
        patientRepository.deleteAll();
        doctorRepository.deleteAll();

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void findById_correctId_patient() throws Exception {
        MvcResult result = mockMvc.perform(get("/patients/{id}", patients.get(4).getPatientId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Marian Garcia"));
        assertTrue(result.getResponse().getContentAsString().contains(doctors.get(5).getEmployeeId().toString()));
    }

    @Test
    void findById_incorrectId_notFound() throws Exception {
        mockMvc.perform(get("/patients/{id}", 0))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findBetweenDateOfBirth_correctDates_listOfPatients() throws Exception {
        MvcResult result = mockMvc.perform(get("/patients/between-date-of-birth?start=1980-01-01&end=2000-01-01"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Jaime Jordan"));
        assertTrue(result.getResponse().getContentAsString().contains("Marian Garcia"));
        assertFalse(result.getResponse().getContentAsString().contains("Steve McDuck"));
    }

    @Test
    void findByDoctorDepartment_correctDepartment_listOfPatients() throws Exception {
        MvcResult result = mockMvc.perform(get("/patients/doctor-department/{department}", "cardiology"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Julia Dusterdieck"));
        assertTrue(result.getResponse().getContentAsString().contains("Steve McDuck"));
    }

    @Test
    void findOffDoctors_correctStatus_listOfPatients() throws Exception {
        MvcResult result = mockMvc.perform(get("/patients/off-doctor"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Marian Garcia"));
        assertTrue(result.getResponse().getContentAsString().contains("Steve McDuck"));
    }

    @Test
    void store_correctPatient_newPatient() throws Exception {
        PatientDTO patientDTO = new PatientDTO("Pepe", "1996-04-29", 356712);
        String body = objectMapper.writeValueAsString(patientDTO);

        MvcResult result = mockMvc.perform(post("/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Pepe"));
    }

    @Test
    void store_incorrectDateOfBirth_badRequest() throws Exception {
        PatientDTO patientDTO = new PatientDTO("Pepe", "1996.04.29", 356712);
        String body = objectMapper.writeValueAsString(patientDTO);

        mockMvc.perform(post("/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void store_incorrectDoctor_notFound() throws Exception {
        PatientDTO patientDTO = new PatientDTO("Pepe", "1996-04-29", 0);
        String body = objectMapper.writeValueAsString(patientDTO);

        mockMvc.perform(post("/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_updateDate_noContent() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setDateOfBirth("1996-04-29");

        String body = objectMapper.writeValueAsString(patientDTO);

        mockMvc.perform(put("/patients/{id}", patients.get(2).getPatientId()).content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();

        Patient patient = patientRepository.findById(patients.get(2).getPatientId()).get();

        assertEquals("Julia Dusterdieck", patient.getName());
        assertEquals("1996-04-29", formatter.format(patient.getDateOfBirth()));
        assertEquals(356712, patient.getAdmittedBy().getEmployeeId());
    }

    @Test
    void update_updateName_noContent() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("Pepe");
        String body = objectMapper.writeValueAsString(patientDTO);

        mockMvc.perform(put("/patients/{id}", patients.get(2).getPatientId()).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        Patient patient = patientRepository.findById(patients.get(2).getPatientId()).get();

        assertEquals("Pepe", patient.getName());
        assertEquals("1954-06-11", formatter.format(patient.getDateOfBirth()));
        assertEquals(356712, patient.getAdmittedBy().getEmployeeId());
    }

    @Test
    void update_updateDoctor_noContent() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setDoctorId(564134);

        String body = objectMapper.writeValueAsString(patientDTO);

        mockMvc.perform(put("/patients/{id}", patients.get(2).getPatientId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        Patient patient = patientRepository.findById(patients.get(2).getPatientId()).get();

        assertEquals("Julia Dusterdieck", patient.getName());
        assertEquals("1954-06-11", formatter.format(patient.getDateOfBirth()));
        assertEquals(564134, patient.getAdmittedBy().getEmployeeId());
    }

    @Test
    void update_incorrectPatientId_notFound() throws Exception {
        String body = objectMapper.writeValueAsString(new PatientDTO());

        mockMvc.perform(put("/patients/{id}", 0)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void update_incorrectDate_badRequest() throws Exception {
        PatientDTO patientDTO = new PatientDTO("Pepe", "1996.04.29", 564134);

        String body = objectMapper.writeValueAsString(patientDTO);

        mockMvc.perform(put("/patients/{id}", patients.get(2).getPatientId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_incorrectDoctorId_badRequest() throws Exception {
        PatientDTO patientDTO = new PatientDTO("Pepe", "1996.04.29", 1_234_567_890);

        String body = objectMapper.writeValueAsString(patientDTO);

        mockMvc.perform(put("/patients/{id}", patients.get(2).getPatientId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        patientDTO = new PatientDTO("Pepe", "1996.04.29", 0);

        body = objectMapper.writeValueAsString(patientDTO);

        mockMvc.perform(put("/patients/{id}", patients.get(2).getPatientId())
                        .content(body)
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
