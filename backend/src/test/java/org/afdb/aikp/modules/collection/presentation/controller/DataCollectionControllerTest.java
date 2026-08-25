package org.afdb.aikp.modules.collection.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.collection.application.response.DataCollectionResponse;
import org.afdb.aikp.modules.collection.application.response.DataCollectionSummary;
import org.afdb.aikp.modules.collection.application.service.DataCollectionApplicationService;
import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.presentation.request.CreateDataCollectionRequest;
import org.afdb.aikp.modules.collection.presentation.request.UpdateDataCollectionRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataCollectionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class DataCollectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DataCollectionApplicationService service;

    @Test
    void shouldCreateDataCollection()
            throws Exception {

        UUID id = UUID.randomUUID();
        UUID campaignId = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();
        UUID questionnaireId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();

        CreateDataCollectionRequest request =
                new CreateDataCollectionRequest(
                        campaignId,
                        countryId,
                        questionnaireId,
                        organizationId,
                        personId);

        DataCollectionResponse response =
                response(
                        id,
                        campaignId,
                        countryId,
                        questionnaireId,
                        organizationId,
                        personId,
                        DataCollectionStatus.DRAFT);

        when(service.createDataCollection(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/data-collections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.id").value(id.toString()))
                .andExpect(
                        jsonPath("$.campaignId")
                                .value(campaignId.toString()))
                .andExpect(
                        jsonPath("$.status").value("DRAFT"));
    }

    @Test
    void shouldGetDataCollectionById()
            throws Exception {

        UUID id = UUID.randomUUID();

        DataCollectionResponse response =
                response(
                        id,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        DataCollectionStatus.DRAFT);

        when(service.getDataCollection(any()))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/data-collections/{id}", id))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(id.toString()))
                .andExpect(
                        jsonPath("$.status").value("DRAFT"));
    }

    @Test
    void shouldGetAllDataCollections()
            throws Exception {

        DataCollectionSummary first =
                summary(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        DataCollectionStatus.DRAFT);

        DataCollectionSummary second =
                summary(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        DataCollectionStatus.IN_PROGRESS);

        when(service.getDataCollections(any()))
                .thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/data-collections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].id")
                                .value(first.id().toString()))
                .andExpect(
                        jsonPath("$[1].id")
                                .value(second.id().toString()));
    }

    @Test
    void shouldGetDataCollectionsByCampaign()
            throws Exception {

        UUID campaignId = UUID.randomUUID();

        DataCollectionSummary first =
                summary(
                        UUID.randomUUID(),
                        campaignId,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        DataCollectionStatus.DRAFT);

        DataCollectionSummary second =
                summary(
                        UUID.randomUUID(),
                        campaignId,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        DataCollectionStatus.IN_PROGRESS);

        when(service.getDataCollectionsByCampaign(any()))
                .thenReturn(List.of(first, second));

        mockMvc.perform(
                        get(
                                "/api/data-collections/campaign/{campaignId}",
                                campaignId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].campaignId")
                                .value(campaignId.toString()))
                .andExpect(
                        jsonPath("$[1].campaignId")
                                .value(campaignId.toString()));
    }

    @Test
    void shouldGetDataCollectionsByCountry()
            throws Exception {

        UUID countryId = UUID.randomUUID();

        DataCollectionSummary first =
                summary(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        countryId,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        DataCollectionStatus.DRAFT);

        DataCollectionSummary second =
                summary(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        countryId,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        DataCollectionStatus.SUBMITTED);

        when(service.getDataCollectionsByCountry(any()))
                .thenReturn(List.of(first, second));

        mockMvc.perform(
                        get(
                                "/api/data-collections/country/{countryId}",
                                countryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].countryId")
                                .value(countryId.toString()))
                .andExpect(
                        jsonPath("$[1].countryId")
                                .value(countryId.toString()));
    }

    @Test
    void shouldUpdateDataCollection()
            throws Exception {

        UUID id = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();

        UpdateDataCollectionRequest request =
                new UpdateDataCollectionRequest(
                        organizationId,
                        personId);

        DataCollectionResponse response =
                response(
                        id,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        organizationId,
                        personId,
                        DataCollectionStatus.DRAFT);

        when(service.updateDataCollection(any()))
                .thenReturn(response);

        mockMvc.perform(
                        put("/api/data-collections/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(id.toString()))
                .andExpect(
                        jsonPath("$.responsibleOrganizationId")
                                .value(organizationId.toString()))
                .andExpect(
                        jsonPath("$.dataCollectorId")
                                .value(personId.toString()));
    }

    @Test
    void shouldStartDataCollection()
            throws Exception {

        UUID id = UUID.randomUUID();

        when(service.startDataCollection(any()))
                .thenReturn(
                        responseWithStatus(
                                id,
                                DataCollectionStatus.IN_PROGRESS));

        mockMvc.perform(
                        post("/api/data-collections/{id}/start", id))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value("IN_PROGRESS"));
    }

    @Test
    void shouldSubmitDataCollection()
            throws Exception {

        UUID id = UUID.randomUUID();

        when(service.submitDataCollection(any()))
                .thenReturn(
                        responseWithStatus(
                                id,
                                DataCollectionStatus.SUBMITTED));

        mockMvc.perform(
                        post("/api/data-collections/{id}/submit", id))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value("SUBMITTED"));
    }

    @Test
    void shouldValidateDataCollection()
            throws Exception {

        UUID id = UUID.randomUUID();

        when(service.validateDataCollection(any()))
                .thenReturn(
                        responseWithStatus(
                                id,
                                DataCollectionStatus.VALIDATED));

        mockMvc.perform(
                        post("/api/data-collections/{id}/validate", id))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value("VALIDATED"));
    }

    @Test
    void shouldRejectDataCollection()
            throws Exception {

        UUID id = UUID.randomUUID();

        when(service.rejectDataCollection(any()))
                .thenReturn(
                        responseWithStatus(
                                id,
                                DataCollectionStatus.REJECTED));

        mockMvc.perform(
                        post("/api/data-collections/{id}/reject", id))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value("REJECTED"));
    }

    @Test
    void shouldCancelDataCollection()
            throws Exception {

        UUID id = UUID.randomUUID();

        when(service.cancelDataCollection(any()))
                .thenReturn(
                        responseWithStatus(
                                id,
                                DataCollectionStatus.CANCELLED));

        mockMvc.perform(
                        post("/api/data-collections/{id}/cancel", id))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value("CANCELLED"));
    }

    @Test
    void shouldDeleteDataCollection()
            throws Exception {

        UUID id = UUID.randomUUID();

        doNothing()
                .when(service)
                .deleteDataCollection(any());

        mockMvc.perform(
                        delete("/api/data-collections/{id}", id))
                .andExpect(status().isNoContent());
    }

    private DataCollectionResponse responseWithStatus(
            UUID id,
            DataCollectionStatus status) {

        return response(
                id,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                status);
    }

    private DataCollectionResponse response(
            UUID id,
            UUID campaignId,
            UUID countryId,
            UUID questionnaireId,
            UUID organizationId,
            UUID personId,
            DataCollectionStatus status) {

        return new DataCollectionResponse(
                id,
                campaignId,
                countryId,
                questionnaireId,
                organizationId,
                personId,
                status);
    }

    private DataCollectionSummary summary(
            UUID id,
            UUID campaignId,
            UUID countryId,
            UUID questionnaireId,
            UUID organizationId,
            UUID personId,
            DataCollectionStatus status) {

        return new DataCollectionSummary(
                id,
                campaignId,
                countryId,
                questionnaireId,
                organizationId,
                personId,
                status);
    }
}
