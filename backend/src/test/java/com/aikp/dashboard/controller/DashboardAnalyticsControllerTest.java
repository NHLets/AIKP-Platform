package com.aikp.dashboard.controller;

import com.aikp.dashboard.dto.SeverityDistributionDto;
import com.aikp.dashboard.service.DashboardAnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardAnalyticsController.class)
class DashboardAnalyticsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DashboardAnalyticsService service;

    @Test
    void shouldReturnSeverityDistribution() throws Exception {

        when(service.getSeverityDistribution(1L, 2026))
                .thenReturn(List.of(
                        new SeverityDistributionDto("ERROR", 12L),
                        new SeverityDistributionDto("WARNING", 5L)
                ));

        mockMvc.perform(get("/api/dashboard/analytics/severity-distribution")
                        .param("campaignId", "1")
                        .param("referenceYear", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].severity").value("ERROR"))
                .andExpect(jsonPath("$[0].count").value(12))
                .andExpect(jsonPath("$[1].severity").value("WARNING"))
                .andExpect(jsonPath("$[1].count").value(5));
    }

    @Test
    void shouldReturnValidationTrend() throws Exception {

        when(service.getValidationTrend(1L, 2026))
                .thenReturn(List.of(
                        new ValidationTrendDto(1, "Jan", 15L),
                        new ValidationTrendDto(2, "Feb", 21L)
                ));

        mockMvc.perform(get("/api/dashboard/analytics/validation-trend")
                        .param("campaignId", "1")
                        .param("referenceYear", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].month").value(1))
                .andExpect(jsonPath("$[0].monthName").value("Jan"))
                .andExpect(jsonPath("$[0].count").value(15))
                .andExpect(jsonPath("$[1].month").value(2))
                .andExpect(jsonPath("$[1].monthName").value("Feb"))
                .andExpect(jsonPath("$[1].count").value(21));
    }


    @Test
    void shouldReturnValidationHeatmap() throws Exception {

        when(service.getValidationHeatmap(1L, 2026))
                .thenReturn(List.of(
                        new ValidationHeatmapDto("POWER-G", "GEN_001", 17L),
                        new ValidationHeatmapDto("POWER-T", "TRN_002", 8L)
                ));

        mockMvc.perform(get("/api/dashboard/analytics/validation-heatmap")
                        .param("campaignId", "1")
                        .param("referenceYear", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionnaire").value("POWER-G"))
                .andExpect(jsonPath("$[0].variable").value("GEN_001"))
                .andExpect(jsonPath("$[0].count").value(17))
                .andExpect(jsonPath("$[1].questionnaire").value("POWER-T"))
                .andExpect(jsonPath("$[1].variable").value("TRN_002"))
                .andExpect(jsonPath("$[1].count").value(8));
    }


    @Test
    void shouldReturnKpiSummary() throws Exception {

        when(service.getKpiSummary(1L, 2026))
                .thenReturn(new KpiSummaryDto(
                        1248L,
                        42L,
                        387L,
                        18L
                ));

        mockMvc.perform(get("/api/dashboard/analytics/kpi-summary")
                        .param("campaignId", "1")
                        .param("referenceYear", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalComments").value(1248))
                .andExpect(jsonPath("$.criticalComments").value(42))
                .andExpect(jsonPath("$.affectedVariables").value(387))
                .andExpect(jsonPath("$.affectedQuestionnaires").value(18));
    }


    @Test
    void shouldReturnDashboardOverview() throws Exception {

        DashboardOverviewDto overview = new DashboardOverviewDto(
                new KpiSummaryDto(1248L, 42L, 387L, 18L),
                List.of(new SeverityDistributionDto("ERROR", 38L)),
                List.of(new ValidationTrendDto(1, "Jan", 15L)),
                List.of(new ValidationHeatmapDto("POWER-G", "GEN_001", 17L))
        );

        when(service.getDashboardOverview(1L, 2026))
                .thenReturn(overview);

        mockMvc.perform(get("/api/dashboard/analytics/overview")
                        .param("campaignId", "1")
                        .param("referenceYear", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kpiSummary.totalComments").value(1248))
                .andExpect(jsonPath("$.severityDistribution[0].severity").value("ERROR"))
                .andExpect(jsonPath("$.validationTrend[0].month").value(1))
                .andExpect(jsonPath("$.validationHeatmap[0].questionnaire").value("POWER-G"));
    }

}