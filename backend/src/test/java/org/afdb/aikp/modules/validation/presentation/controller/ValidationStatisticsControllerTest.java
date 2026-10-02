package org.afdb.aikp.modules.validation.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.validation.application.dto.SeverityDistributionDto;
import org.afdb.aikp.modules.validation.application.dto.ValidationHeatmapDto;
import org.afdb.aikp.modules.validation.application.service.ValidationStatisticsService;
import org.afdb.aikp.shared.security.JwtService;
import org.afdb.aikp.shared.security.AikpUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ValidationStatisticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class ValidationStatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidationStatisticsService service;


    @MockBean
    private JwtService jwtService;

    @MockBean
    private AikpUserDetailsService userDetailsService;

    private static final UUID DATA_COLLECTION_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Test
    void shouldReturnSeverityDistribution() throws Exception {

        when(service.getSeverityDistribution(DATA_COLLECTION_ID))
                .thenReturn(List.of(
                        new SeverityDistributionDto("ERROR", 12L),
                        new SeverityDistributionDto("WARNING", 5L)
                ));

        mockMvc.perform(get(
                "/api/validation/statistics/{dataCollectionId}/severity",
                DATA_COLLECTION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].severity").value("ERROR"))
                .andExpect(jsonPath("$[0].count").value(12))
                .andExpect(jsonPath("$[1].severity").value("WARNING"))
                .andExpect(jsonPath("$[1].count").value(5));
    }

    @Test
    void shouldReturnValidationHeatmap() throws Exception {

        when(service.getValidationHeatmap(DATA_COLLECTION_ID))
                .thenReturn(List.of(
                        new ValidationHeatmapDto(
                                "POWER-G",
                                "GEN_001",
                                1,
                                17L
                        ),
                        new ValidationHeatmapDto(
                                "POWER-T",
                                "TRN_002",
                                2,
                                8L
                        )
                ));

        mockMvc.perform(get(
                "/api/validation/statistics/{dataCollectionId}/heatmap",
                DATA_COLLECTION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionnaireCode").value("POWER-G"))
                .andExpect(jsonPath("$[0].variableCode").value("GEN_001"))
                .andExpect(jsonPath("$[0].validationStatus").value(1))
                .andExpect(jsonPath("$[0].count").value(17))
                .andExpect(jsonPath("$[1].questionnaireCode").value("POWER-T"))
                .andExpect(jsonPath("$[1].variableCode").value("TRN_002"))
                .andExpect(jsonPath("$[1].validationStatus").value(2))
                .andExpect(jsonPath("$[1].count").value(8));
    }
}
