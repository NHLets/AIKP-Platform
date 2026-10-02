package org.afdb.aikp.shared.security;

import org.afdb.aikp.modules.iam.application.service.UserApplicationService;
import org.afdb.aikp.modules.iam.presentation.controller.UserController;
import org.afdb.aikp.shared.configuration.SecurityConfig;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class RbacSecurityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserApplicationService userApplicationService;

    @MockBean
    JwtService jwtService;

    @MockBean
    AikpUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldAccessUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "COORDINATOR")
    void coordinatorShouldBeForbiddenOnUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void anonymousShouldBeUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());
    }
}
