package org.afdb.aikp.modules.collection.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.collection.application.observation.response.DataCollectionObservationResponse;
import org.afdb.aikp.modules.collection.application.observation.service.DataCollectionObservationApplicationService;
import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;
import org.afdb.aikp.modules.collection.presentation.request.CreateDataCollectionObservationRequest;
import org.afdb.aikp.modules.collection.presentation.request.UpdateDataCollectionObservationRequest;
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

@WebMvcTest(DataCollectionObservationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class DataCollectionObservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DataCollectionObservationApplicationService service;

    @Test
    void shouldCreateProvidedObservation()
            throws Exception {

        UUID id = UUID.randomUUID();
        UUID dataCollectionId = UUID.randomUUID();
        UUID questionnaireVariableId = UUID.randomUUID();

        CreateDataCollectionObservationRequest request =
                new CreateDataCollectionObservationRequest(
                        dataCollectionId,
                        questionnaireVariableId,
                        2024,
                        new BigDecimal("123.45"),
                        null,
                        null,
                        null,
                        "MW",
                        "Test comment");

        DataCollectionObservationResponse response =
                response(
                        id,
                        dataCollectionId,
                        questionnaireVariableId,
                        ObservationStatus.PROVIDED,
                        2024,
                        new BigDecimal("123.45"));

        when(service.createProvidedObservation(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/data-collection-observations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.id").value(id.toString()))
                .andExpect(
                        jsonPath("$.dataCollectionId")
                                .value(dataCollectionId.toString()))
                .andExpect(
                        jsonPath("$.questionnaireVariableId")
                                .value(questionnaireVariableId.toString()))
                .andExpect(
                        jsonPath("$.status").value("PROVIDED"))
                .andExpect(
                        jsonPath("$.numericValue").value(123.45));
    }

    @Test
    void shouldCreateNotAvailableObservation()
            throws Exception {

        UUID id = UUID.randomUUID();
        UUID dataCollectionId = UUID.randomUUID();
        UUID questionnaireVariableId = UUID.randomUUID();

        CreateDataCollectionObservationRequest request =
                new CreateDataCollectionObservationRequest(
                        dataCollectionId,
                        questionnaireVariableId,
                        2024,
                        null,
                        null,
                        null,
                        null,
                        "MW",
                        "Not available");

        when(service.createNotAvailableObservation(any()))
                .thenReturn(
                        response(
                                id,
                                dataCollectionId,
                                questionnaireVariableId,
                                ObservationStatus.NOT_AVAILABLE,
                                2024,
                                null));

        mockMvc.perform(
                        post(
                                "/api/data-collection-observations/"
                                        + "not-available")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.status")
                                .value("NOT_AVAILABLE"));
    }

    @Test
    void shouldCreateNotApplicableObservation()
            throws Exception {

        UUID id = UUID.randomUUID();
        UUID dataCollectionId = UUID.randomUUID();
        UUID questionnaireVariableId = UUID.randomUUID();

        CreateDataCollectionObservationRequest request =
                new CreateDataCollectionObservationRequest(
                        dataCollectionId,
                        questionnaireVariableId,
                        2024,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "Not applicable");

        when(service.createNotApplicableObservation(any()))
                .thenReturn(
                        response(
                                id,
                                dataCollectionId,
                                questionnaireVariableId,
                                ObservationStatus.NOT_APPLICABLE,
                                2024,
                                null));

        mockMvc.perform(
                        post(
                                "/api/data-collection-observations/"
                                        + "not-applicable")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.status")
                                .value("NOT_APPLICABLE"));
    }

    @Test
    void shouldUpdateObservation()
            throws Exception {

        UUID id = UUID.randomUUID();

        UpdateDataCollectionObservationRequest request =
                new UpdateDataCollectionObservationRequest(
                        new BigDecimal("456.78"),
                        null,
                        null,
                        null,
                        "MW",
                        "Updated");

        when(service.updateObservation(any()))
                .thenReturn(
                        response(
                                id,
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                ObservationStatus.PROVIDED,
                                2024,
                                new BigDecimal("456.78")));

        mockMvc.perform(
                        put(
                                "/api/data-collection-observations/{id}",
                                id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(id.toString()))
                .andExpect(
                        jsonPath("$.numericValue")
                                .value(456.78));
    }

    @Test
    void shouldDeleteObservation()
            throws Exception {

        UUID id = UUID.randomUUID();

        doNothing()
                .when(service)
                .deleteObservation(any());

        mockMvc.perform(
                        delete(
                                "/api/data-collection-observations/{id}",
                                id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetObservationById()
            throws Exception {

        UUID id = UUID.randomUUID();

        when(service.getObservation(any()))
                .thenReturn(
                        response(
                                id,
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                ObservationStatus.PROVIDED,
                                2024,
                                new BigDecimal("100")));

        mockMvc.perform(
                        get(
                                "/api/data-collection-observations/{id}",
                                id))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(id.toString()))
                .andExpect(
                        jsonPath("$.status")
                                .value("PROVIDED"));
    }

    @Test
    void shouldGetObservationsByCollection()
            throws Exception {

        UUID dataCollectionId = UUID.randomUUID();

        DataCollectionObservationResponse first =
                response(
                        UUID.randomUUID(),
                        dataCollectionId,
                        UUID.randomUUID(),
                        ObservationStatus.PROVIDED,
                        2023,
                        new BigDecimal("100"));

        DataCollectionObservationResponse second =
                response(
                        UUID.randomUUID(),
                        dataCollectionId,
                        UUID.randomUUID(),
                        ObservationStatus.NOT_AVAILABLE,
                        2024,
                        null);

        when(service.getObservations(any()))
                .thenReturn(List.of(first, second));

        mockMvc.perform(
                        get(
                                "/api/data-collection-observations/"
                                        + "collection/{dataCollectionId}",
                                dataCollectionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].dataCollectionId")
                                .value(dataCollectionId.toString()))
                .andExpect(
                        jsonPath("$[1].dataCollectionId")
                                .value(dataCollectionId.toString()));
    }

    @Test
    void shouldGetObservationsByVariable()
            throws Exception {

        UUID questionnaireVariableId = UUID.randomUUID();

        DataCollectionObservationResponse first =
                response(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        questionnaireVariableId,
                        ObservationStatus.PROVIDED,
                        2023,
                        new BigDecimal("100"));

        DataCollectionObservationResponse second =
                response(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        questionnaireVariableId,
                        ObservationStatus.PROVIDED,
                        2024,
                        new BigDecimal("110"));

        when(service.getObservationsByVariable(any()))
                .thenReturn(List.of(first, second));

        mockMvc.perform(
                        get(
                                "/api/data-collection-observations/"
                                        + "variable/{questionnaireVariableId}",
                                questionnaireVariableId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].questionnaireVariableId")
                                .value(
                                        questionnaireVariableId.toString()))
                .andExpect(
                        jsonPath("$[1].questionnaireVariableId")
                                .value(
                                        questionnaireVariableId.toString()));
    }

    @Test
    void shouldGetObservationByCollectionVariableYear()
            throws Exception {

        UUID dataCollectionId = UUID.randomUUID();
        UUID questionnaireVariableId = UUID.randomUUID();

        when(service.getObservationByCollectionVariableYear(any()))
                .thenReturn(
                        response(
                                UUID.randomUUID(),
                                dataCollectionId,
                                questionnaireVariableId,
                                ObservationStatus.PROVIDED,
                                2024,
                                new BigDecimal("123.45")));

        mockMvc.perform(
                        get("/api/data-collection-observations/lookup")
                                .param(
                                        "dataCollectionId",
                                        dataCollectionId.toString())
                                .param(
                                        "questionnaireVariableId",
                                        questionnaireVariableId.toString())
                                .param(
                                        "referenceYear",
                                        "2024"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.dataCollectionId")
                                .value(dataCollectionId.toString()))
                .andExpect(
                        jsonPath("$.questionnaireVariableId")
                                .value(
                                        questionnaireVariableId.toString()))
                .andExpect(
                        jsonPath("$.referenceYear")
                                .value(2024));
    }

    private DataCollectionObservationResponse response(
            UUID id,
            UUID dataCollectionId,
            UUID questionnaireVariableId,
            ObservationStatus status,
            int referenceYear,
            BigDecimal numericValue) {

        return new DataCollectionObservationResponse(
                id,
                dataCollectionId,
                questionnaireVariableId,
                referenceYear,
                status,
                numericValue,
                null,
                null,
                null,
                "MW",
                "Test comment");
    }
}
