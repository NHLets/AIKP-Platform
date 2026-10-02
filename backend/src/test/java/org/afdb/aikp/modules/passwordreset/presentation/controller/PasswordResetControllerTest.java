package org.afdb.aikp.modules.passwordreset.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.afdb.aikp.modules.passwordreset.application.service.PasswordResetApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.afdb.aikp.shared.security.AikpUserDetailsService;
import org.afdb.aikp.shared.security.JwtService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PasswordResetController.class)
@AutoConfigureMockMvc(addFilters = false)
class PasswordResetControllerTest {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AikpUserDetailsService userDetailsService;


    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    @MockBean
    PasswordResetApplicationService service;

    @Test
    void shouldForgotPassword() throws Exception {
        mockMvc.perform(post("/api/v1/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@afdb.org\"}"))
                .andExpect(status().isOk());

        verify(service).forgotPassword("john@afdb.org");
    }

    @Test
    void shouldValidateToken() throws Exception {

        when(service.validateToken("abc")).thenReturn(true);

        mockMvc.perform(get("/api/v1/auth/reset-password/validate")
                .param("token","abc"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldResetPassword() throws Exception {

        mockMvc.perform(post("/api/v1/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"abc\",\"password\":\"Secret123!\"}"))
                .andExpect(status().isOk());

        verify(service).resetPassword("abc","Secret123!");
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {

        mockMvc.perform(post("/api/v1/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"bad-email\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectBlankPassword() throws Exception {

        mockMvc.perform(post("/api/v1/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"abc\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
