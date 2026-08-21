package org.afdb.aikp.modules.campaign.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.afdb.aikp.modules.campaign.application.response.CampaignResponse;
import org.afdb.aikp.modules.campaign.application.service.CampaignApplicationService;
import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignCodeAlreadyExistsException;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignLifecycleException;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignNotFoundException;
import org.afdb.aikp.modules.campaign.presentation.request.CreateCampaignRequest;
import org.afdb.aikp.modules.campaign.presentation.request.UpdateCampaignRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(CampaignController.class)
@AutoConfigureMockMvc(addFilters = false)
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CampaignApplicationService service;

    private static final UUID CAMPAIGN_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private CampaignResponse response() {

        return new CampaignResponse(
                CAMPAIGN_ID,
                "AIKP_2026",
                "AIKP Data Collection 2026",
                "AIKP infrastructure data collection campaign.",
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 10, 30),
                CampaignStatus.DRAFT,
                false);
    }

    @Test
    void shouldCreateCampaign() throws Exception {

        CreateCampaignRequest request =
                new CreateCampaignRequest(
                        "AIKP_2026",
                        "AIKP Data Collection 2026",
                        "AIKP infrastructure data collection campaign.",
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 10, 30));

        when(service.create(any()))
                .thenReturn(response());

        mockMvc.perform(
                        post("/api/v1/campaigns")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "/api/v1/campaigns/" + CAMPAIGN_ID))
                .andExpect(jsonPath("$.id")
                        .value(CAMPAIGN_ID.toString()))
                .andExpect(jsonPath("$.code")
                        .value("AIKP_2026"))
                .andExpect(jsonPath("$.status")
                        .value("DRAFT"))
                .andExpect(jsonPath("$.active")
                        .value(false));
    }

    @Test
void shouldReturn409WhenCampaignCodeAlreadyExists()
        throws Exception {

    CreateCampaignRequest request =
            new CreateCampaignRequest(
                    "AIKP_2026",
                    "AIKP Data Collection 2026",
                    "AIKP infrastructure data collection campaign.",
                    LocalDate.of(2026, 8, 1),
                    LocalDate.of(2026, 10, 30));

    when(service.create(any()))
            .thenThrow(
                    new CampaignCodeAlreadyExistsException(
                            "Campaign code already exists: AIKP_2026"));

    mockMvc.perform(
                    post("/api/v1/campaigns")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    objectMapper.writeValueAsString(
                                            request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.title")
                    .value("Conflict"))
            .andExpect(jsonPath("$.status")
                    .value(409))
            .andExpect(jsonPath("$.errorCode")
                    .value("CONFLICT"));
    }
    
    @Test
    void shouldGetCampaignById() throws Exception {

        when(service.getById(any()))
                .thenReturn(response());

        mockMvc.perform(
                        get("/api/v1/campaigns/" + CAMPAIGN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(CAMPAIGN_ID.toString()))
                .andExpect(jsonPath("$.code")
                        .value("AIKP_2026"));
    }

    @Test
    void shouldGetCampaignByCode() throws Exception {

        when(service.getByCode("AIKP_2026"))
                .thenReturn(response());

        mockMvc.perform(
                        get("/api/v1/campaigns/code/AIKP_2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code")
                        .value("AIKP_2026"));
    }

    @Test
    void shouldUpdateCampaign() throws Exception {

        UpdateCampaignRequest request =
                new UpdateCampaignRequest(
                        "Updated Campaign",
                        "Updated description.",
                        LocalDate.of(2026, 8, 5),
                        LocalDate.of(2026, 11, 5));

        CampaignResponse updated =
                new CampaignResponse(
                        CAMPAIGN_ID,
                        "AIKP_2026",
                        "Updated Campaign",
                        "Updated description.",
                        LocalDate.of(2026, 8, 5),
                        LocalDate.of(2026, 11, 5),
                        CampaignStatus.DRAFT,
                        false);

        when(service.update(any()))
                .thenReturn(updated);

        mockMvc.perform(
                        put("/api/v1/campaigns/" + CAMPAIGN_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Updated Campaign"))
                .andExpect(jsonPath("$.description")
                        .value("Updated description."));
    }

    @Test
    void shouldPlanCampaign() throws Exception {

        when(service.plan(any()))
                .thenReturn(response());

        mockMvc.perform(
                        patch("/api/v1/campaigns/"
                                + CAMPAIGN_ID + "/plan"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldActivateCampaign() throws Exception {

        CampaignResponse active =
                new CampaignResponse(
                        CAMPAIGN_ID,
                        "AIKP_2026",
                        "AIKP Data Collection 2026",
                        "AIKP infrastructure data collection campaign.",
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 10, 30),
                        CampaignStatus.ACTIVE,
                        true);

        when(service.activate(any()))
                .thenReturn(active);

        mockMvc.perform(
                        patch("/api/v1/campaigns/"
                                + CAMPAIGN_ID + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("ACTIVE"))
                .andExpect(jsonPath("$.active")
                        .value(true));
    }

@Test
void shouldReturn409ForInvalidLifecycleTransition()
        throws Exception {

    when(service.activate(any()))
            .thenThrow(
                    new CampaignLifecycleException(
                            "Campaign cannot be activated from status DRAFT."));

    mockMvc.perform(
                    patch("/api/v1/campaigns/"
                            + CAMPAIGN_ID
                            + "/activate"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.title")
                    .value("Conflict"))
            .andExpect(jsonPath("$.status")
                    .value(409))
            .andExpect(jsonPath("$.errorCode")
                    .value("CONFLICT"));
    }

    @Test
    void shouldCompleteCampaign() throws Exception {

        CampaignResponse completed =
                new CampaignResponse(
                        CAMPAIGN_ID,
                        "AIKP_2026",
                        "AIKP Data Collection 2026",
                        "AIKP infrastructure data collection campaign.",
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 10, 30),
                        CampaignStatus.COMPLETED,
                        false);

        when(service.complete(any()))
                .thenReturn(completed);

        mockMvc.perform(
                        patch("/api/v1/campaigns/"
                                + CAMPAIGN_ID + "/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("COMPLETED"));
    }

    @Test
    void shouldArchiveCampaign() throws Exception {

        CampaignResponse archived =
                new CampaignResponse(
                        CAMPAIGN_ID,
                        "AIKP_2026",
                        "AIKP Data Collection 2026",
                        "AIKP infrastructure data collection campaign.",
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 10, 30),
                        CampaignStatus.ARCHIVED,
                        false);

        when(service.archive(any()))
                .thenReturn(archived);

        mockMvc.perform(
                        patch("/api/v1/campaigns/"
                                + CAMPAIGN_ID + "/archive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("ARCHIVED"));
    }

    @Test
    void shouldReturn404WhenCampaignDoesNotExist() throws Exception {

    when(service.getById(any()))
            .thenThrow(
                    new CampaignNotFoundException(
                            "Campaign not found with id: "
                                    + CAMPAIGN_ID));

    mockMvc.perform(
                    get("/api/v1/campaigns/" + CAMPAIGN_ID))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.title")
                    .value("Not Found"))
            .andExpect(jsonPath("$.status")
                    .value(404))
            .andExpect(jsonPath("$.detail")
                    .value(
                            "Campaign not found with id: "
                                    + CAMPAIGN_ID));
    }
    
    @Test
    void shouldDeleteCampaign() throws Exception {

        doNothing().when(service).delete(any());

        mockMvc.perform(
                        delete("/api/v1/campaigns/" + CAMPAIGN_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRejectInvalidCreateRequest() throws Exception {

        CreateCampaignRequest request =
                new CreateCampaignRequest(
                        "",
                        "",
                        "",
                        null,
                        null);

        mockMvc.perform(
                        post("/api/v1/campaigns")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request)))
                .andExpect(status().isBadRequest());
    }
}
