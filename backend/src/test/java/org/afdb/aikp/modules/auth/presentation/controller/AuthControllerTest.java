package org.afdb.aikp.modules.auth.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.afdb.aikp.modules.auth.application.dto.LoginRequestDto;
import org.afdb.aikp.modules.auth.application.dto.LoginResponseDto;
import org.afdb.aikp.modules.auth.application.service.AuthApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.afdb.aikp.shared.security.AikpUserDetailsService;
import org.afdb.aikp.shared.security.JwtService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthApplicationService service;

    @MockBean
    JwtService jwtService;

    @MockBean
    AikpUserDetailsService userDetailsService;

    @Test
    void shouldLoginSuccessfully() throws Exception {

        var request = new LoginRequestDto(
                "admin@aikp.org",
                "password"
        );

        when(service.login(request)).thenReturn(
                new LoginResponseDto(
                        "jwt-token",
                        "Bearer",
                        "00000000-0000-0000-0000-000000000001",
                        "admin@aikp.org",
                        "ADMIN"
                )
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.email").value("admin@aikp.org"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}
