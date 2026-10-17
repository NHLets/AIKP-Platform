package org.afdb.aikp.modules.validation.presentation.controller;

import org.afdb.aikp.modules.validation.application.response.DataCollectionValidationResponse;
import org.afdb.aikp.modules.validation.application.service.DataCollectionValidationApplicationService;
import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;
import org.afdb.aikp.modules.validation.presentation.request.CreateDataCollectionValidationRequest;
import org.afdb.aikp.shared.configuration.SecurityConfig;
import org.afdb.aikp.shared.security.AikpUserDetailsService;
import org.afdb.aikp.shared.security.JwtService;
import org.afdb.aikp.shared.web.ApiExceptionHandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataCollectionValidationController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class, ApiExceptionHandler.class})
class DataCollectionValidationSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DataCollectionValidationApplicationService service;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AikpUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "VALIDATOR")
    void validatorShouldAccessValidationHistory() throws Exception {
        UUID validationId = UUID.randomUUID();

        when(service.get(any())).thenReturn(
                new DataCollectionValidationResponse(
                        validationId,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        null,
                        ValidationDecision.VALIDATED,
                        "Validated",
                        Instant.now()));

        mockMvc.perform(get("/api/data-collection-validations/{id}", validationId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "VALIDATOR")
    void validatorShouldCreateValidation() throws Exception {
        UUID collectionId = UUID.randomUUID();
        UUID validationId = UUID.randomUUID();
        Instant validatedAt = Instant.now();

        CreateDataCollectionValidationRequest request =
                new CreateDataCollectionValidationRequest(
                        collectionId,
                        UUID.randomUUID(),
                        ValidationDecision.VALIDATED,
                        "Validated",
                        validatedAt);

        when(service.createForAuthenticatedUser(
                eq(collectionId),
                eq("user"),
                eq(ValidationDecision.VALIDATED),
                eq("Validated"),
                eq(validatedAt)))
                .thenReturn(new DataCollectionValidationResponse(
                        validationId,
                        collectionId,
                        UUID.randomUUID(),
                        null,
                        ValidationDecision.VALIDATED,
                        "Validated",
                        validatedAt));

        mockMvc.perform(post("/api/data-collection-validations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldDeleteValidation() throws Exception {
        doNothing().when(service).delete(any());

        mockMvc.perform(delete(
                        "/api/data-collection-validations/{id}",
                        UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "VALIDATOR")
    void validatorShouldNotDeleteValidation() throws Exception {
        mockMvc.perform(delete(
                        "/api/data-collection-validations/{id}",
                        UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userWithoutAllowedRoleShouldNotReadValidationHistory() throws Exception {
        mockMvc.perform(get(
                        "/api/data-collection-validations/{id}",
                        UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    void anonymousShouldBeUnauthorized() throws Exception {
        mockMvc.perform(get(
                        "/api/data-collection-validations/{id}",
                        UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }
}
