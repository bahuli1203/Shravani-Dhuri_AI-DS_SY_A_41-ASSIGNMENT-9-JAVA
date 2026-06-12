package com.example.studentapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentSecurityIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getStudents_withoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void getStudents_withUserRole_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getStudents_withAdminRole_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk());
    }

    @Test
    public void createStudent_withoutAuth_shouldReturn401() throws Exception {
        String studentJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john.doe@test.com",
                    "age": 21,
                    "course": "Computer Science"
                }
                """;

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void createStudent_withUserRole_shouldReturn403() throws Exception {
        String studentJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john.doe@test.com",
                    "age": 21,
                    "course": "Computer Science"
                }
                """;

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createStudent_withAdminRole_shouldReturn201() throws Exception {
        String studentJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john.doe@test.com",
                    "age": 21,
                    "course": "Computer Science"
                }
                """;

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated());
    }


    @Test
    public void getStudents_withRealUserCredentials_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/students").with(httpBasic("user", "user123")))
                .andExpect(status().isOk());
    }

    @Test
    public void getStudents_withRealAdminCredentials_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/students").with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk());
    }

    @Test
    public void getStudents_withWrongCredentials_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/students").with(httpBasic("user", "wrongpassword")))
                .andExpect(status().isUnauthorized());
    }
}
