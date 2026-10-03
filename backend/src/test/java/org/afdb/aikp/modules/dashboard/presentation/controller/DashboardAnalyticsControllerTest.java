package org.afdb.aikp.modules.dashboard.presentation.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.dashboard.application.service.DashboardAnalyticsApplicationService;
import org.afdb.aikp.modules.dashboard.dto.DashboardOverviewDto;
import org.afdb.aikp.modules.dashboard.dto.KpiSummaryDto;
import org.afdb.aikp.modules.dashboard.dto.SeverityDistributionDto;
import org.afdb.aikp.modules.dashboard.dto.ValidationHeatmapDto;
import org.afdb.aikp.modules.dashboard.dto.ValidationTrendDto;
import org.afdb.aikp.shared.security.AikpUserDetailsService;
import org.afdb.aikp.shared.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardAnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardAnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardAnalyticsApplicationService service;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AikpUserDetailsService userDetailsService;

    private static final UUID CAMPAIGN_ID =
            UUID.fromString(
                    "11111111-1111-1111-1111-111111111111");

    private static final Integer REFERENCE_YEAR = 2025;

    @Test
    void shouldReturnDashboardAnalyticsOverview()
            throws Exception {

        DashboardOverviewDto response =
                new DashboardOverviewDto(
                        new KpiSummaryDto(
                                10L,
                                3L,
                                7L,
                                2L),
                        List.of(
                                new SeverityDistributionDto(
                                        "CRITICAL",
                                        3L),
                                new SeverityDistributionDto(
                                        "HIGH",
                                        4L)),
                        List.of(
                                new ValidationTrendDto(
                                        "2025-01",
                                        5L,
                                        1L),
                                new ValidationTrendDto(
                                        "2025-02",
                                        8L,
                                        2L)),
                        List.of(
                                new ValidationHeatmapDto(
                                        "PW_B_TEST",
                                        "B001",
                                        4L),
                                new ValidationHeatmapDto(
                                        "PW_B_TEST",
                                        "B002",
                                        6L)));

        when(service.getOverview(
                CAMPAIGN_ID,
                REFERENCE_YEAR))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/dashboard/analytics/overview")
                                .param(
                                        "campaignId",
                                        CAMPAIGN_ID.toString())
                                .param(
                                        "referenceYear",
                                        REFERENCE_YEAR.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.kpiSummary.totalComments")
                        .value(10))
                .andExpect(jsonPath(
                        "$.kpiSummary.criticalComments")
                        .value(3))
                .andExpect(jsonPath(
                        "$.kpiSummary.affectedVariables")
                        .value(7))
                .andExpect(jsonPath(
                        "$.kpiSummary.affectedQuestionnaires")
                        .value(2))
                .andExpect(jsonPath(
                        "$.severityDistribution[0].severity")
                        .value("CRITICAL"))
                .andExpect(jsonPath(
                        "$.severityDistribution[0].count")
                        .value(3))
                .andExpect(jsonPath(
                        "$.validationTrend[0].period")
                        .value("2025-01"))
                .andExpect(jsonPath(
                        "$.validationTrend[0].validated")
                        .value(5))
                .andExpect(jsonPath(
                        "$.validationTrend[0].rejected")
                        .value(1))
                .andExpect(jsonPath(
                        "$.validationHeatmap[0].questionnaire")
                        .value("PW_B_TEST"))
                .andExpect(jsonPath(
                        "$.validationHeatmap[0].variable")
                        .value("B001"))
                .andExpect(jsonPath(
                        "$.validationHeatmap[0].count")
                        .value(4));
    }

    @Test
    void shouldRejectInvalidCampaignId()
            throws Exception {

        mockMvc.perform(
                        get("/api/dashboard/analytics/overview")
                                .param(
                                        "campaignId",
                                        "not-a-uuid")
                                .param(
                                        "referenceYear",
                                        REFERENCE_YEAR.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectMissingCampaignId()
            throws Exception {

        mockMvc.perform(
                        get("/api/dashboard/analytics/overview")
                                .param(
                                        "referenceYear",
                                        REFERENCE_YEAR.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectMissingReferenceYear()
            throws Exception {

        mockMvc.perform(
                        get("/api/dashboard/analytics/overview")
                                .param(
                                        "campaignId",
                                        CAMPAIGN_ID.toString()))
                .andExpect(status().isBadRequest());
    }
}
