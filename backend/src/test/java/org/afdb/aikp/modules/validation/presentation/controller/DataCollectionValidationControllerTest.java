package org.afdb.aikp.modules.validation.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.validation.application.response.DataCollectionValidationResponse;
import org.afdb.aikp.modules.validation.application.response.DataCollectionValidationSummary;
import org.afdb.aikp.modules.validation.application.service.DataCollectionValidationApplicationService;
import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;
import org.afdb.aikp.modules.validation.presentation.request.CreateDataCollectionValidationRequest;
import org.afdb.aikp.shared.web.ApiExceptionHandler;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataCollectionValidationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class DataCollectionValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DataCollectionValidationApplicationService service;

    @Test
    void shouldCreateDataCollectionValidation()
            throws Exception {

        UUID id = UUID.randomUUID();
        UUID dataCollectionId = UUID.randomUUID();
        UUID validatorId = UUID.randomUUID();
        Instant validatedAt = Instant.now();

        CreateDataCollectionValidationRequest request =
                new CreateDataCollectionValidationRequest(
                        dataCollectionId,
                        validatorId,
                        ValidationDecision.VALIDATED,
                        "Validation successful.",
                        validatedAt);

        DataCollectionValidationResponse response =
                new DataCollectionValidationResponse(
                        id,
                        dataCollectionId,
                        validatorId,
                        ValidationDecision.VALIDATED,
                        "Validation successful.",
                        validatedAt);

        when(service.create(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/data-collection-validations")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.id")
                                .value(id.toString()))
                .andExpect(
                        jsonPath("$.dataCollectionId")
                                .value(
                                        dataCollectionId.toString()))
                .andExpect(
                        jsonPath("$.validatorId")
                                .value(validatorId.toString()))
                .andExpect(
                        jsonPath("$.decision")
                                .value("VALIDATED"));
    }

    @Test
    void shouldGetDataCollectionValidationById()
            throws Exception {

        UUID id = UUID.randomUUID();
        UUID dataCollectionId = UUID.randomUUID();
        UUID validatorId = UUID.randomUUID();
        Instant validatedAt = Instant.now();

        DataCollectionValidationResponse response =
                new DataCollectionValidationResponse(
                        id,
                        dataCollectionId,
                        validatorId,
                        ValidationDecision.REJECTED,
                        "Incomplete data.",
                        validatedAt);

        when(service.get(any()))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/data-collection-validations/{id}",
                                id))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(id.toString()))
                .andExpect(
                        jsonPath("$.decision")
                                .value("REJECTED"));
    }

    @Test
    void shouldGetDataCollectionValidationsByDataCollection()
            throws Exception {

        UUID dataCollectionId = UUID.randomUUID();

        DataCollectionValidationSummary first =
                new DataCollectionValidationSummary(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        ValidationDecision.VALIDATED,
                        Instant.now());

        DataCollectionValidationSummary second =
                new DataCollectionValidationSummary(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        ValidationDecision.REJECTED,
                        Instant.now());

        when(service.getByDataCollection(any()))
                .thenReturn(List.of(first, second));

        mockMvc.perform(
                        get(
                                "/api/data-collection-validations"
                                        + "/data-collection/{dataCollectionId}",
                                dataCollectionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].decision")
                                .value("VALIDATED"))
                .andExpect(
                        jsonPath("$[1].decision")
                                .value("REJECTED"));
    }

    @Test
    void shouldDeleteDataCollectionValidation()
            throws Exception {

        UUID id = UUID.randomUUID();

        doNothing()
                .when(service)
                .delete(any());

        mockMvc.perform(
                        delete(
                                "/api/data-collection-validations/{id}",
                                id))
                .andExpect(status().isNoContent());
    }
}
