package org.afdb.aikp.modules.iam.presentation.controller;

import org.afdb.aikp.modules.iam.application.service.UserApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserApplicationService service;

    private final UUID id = UUID.randomUUID();

    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUserById() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateUser() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        mockMvc.perform(put("/api/v1/users/" + id)
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldActivateUser() throws Exception {
        mockMvc.perform(post("/api/v1/users/" + id + "/activate"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeactivateUser() throws Exception {
        mockMvc.perform(post("/api/v1/users/" + id + "/deactivate"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldLockUser() throws Exception {
        mockMvc.perform(post("/api/v1/users/" + id + "/lock"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUnlockUser() throws Exception {
        mockMvc.perform(post("/api/v1/users/" + id + "/unlock"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSuspendUser() throws Exception {
        mockMvc.perform(post("/api/v1/users/" + id + "/suspend"))
                .andExpect(status().isOk());
    }


    @Test
    void shouldAssignRole() throws Exception {

        mockMvc.perform(
                put("/api/v1/users/" + id + "/role")
                        .contentType("application/json")
                        .content("""
                                {
                                  "roleId":"00000000-0000-0000-0000-000000000001"
                                }
                                """)
        ).andExpect(status().isOk());
    }

}