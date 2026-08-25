package org.afdb.aikp.modules.dashboard.presentation.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.afdb.aikp.modules.dashboard.application.query.GetDashboardQuery;
import org.afdb.aikp.modules.dashboard.application.response.DashboardResponse;
import org.afdb.aikp.modules.dashboard.application.service.DashboardApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardApplicationService
            dashboardApplicationService;

    @Test
    void shouldGetDashboard()
            throws Exception {

        DashboardResponse response =
                new DashboardResponse(
                        10,
                        5,
                        20,
                        30,
                        4,
                        8,
                        6,
                        10,
                        2,
                        100,
                        80);

        when(dashboardApplicationService.handle(
                new GetDashboardQuery()))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.totalCampaigns")
                        .value(10))
                .andExpect(jsonPath(
                        "$.activeCampaigns")
                        .value(5))
                .andExpect(jsonPath(
                        "$.totalCountries")
                        .value(20))
                .andExpect(jsonPath(
                        "$.totalDataCollections")
                        .value(30))
                .andExpect(jsonPath(
                        "$.draftDataCollections")
                        .value(4))
                .andExpect(jsonPath(
                        "$.inProgressDataCollections")
                        .value(8))
                .andExpect(jsonPath(
                        "$.submittedDataCollections")
                        .value(6))
                .andExpect(jsonPath(
                        "$.validatedDataCollections")
                        .value(10))
                .andExpect(jsonPath(
                        "$.rejectedDataCollections")
                        .value(2))
                .andExpect(jsonPath(
                        "$.totalPersons")
                        .value(100))
                .andExpect(jsonPath(
                        "$.activePersons")
                        .value(80));
    }
}
