package org.afdb.aikp.modules.quality.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.quality.application.dto.QualityAssessmentResponse;
import org.afdb.aikp.modules.quality.application.mapper.QualityAssessmentApplicationMapper;
import org.afdb.aikp.modules.quality.application.service.QualityAssessmentService;
import org.afdb.aikp.modules.quality.domain.enums.QualityDimension;
import org.afdb.aikp.modules.quality.domain.enums.QualityDimensionStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityLevel;
import org.afdb.aikp.modules.quality.domain.model.QualityAssessment;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.shared.security.AikpUserDetailsService;
import org.afdb.aikp.shared.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QualityAssessmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class QualityAssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QualityAssessmentService service;

    @MockBean
    private QualityAssessmentApplicationMapper mapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AikpUserDetailsService userDetailsService;

    private static final UUID DATA_COLLECTION_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID QUALITY_RUN_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void shouldReturnQualityAssessment() throws Exception {

        QualityAssessmentResponse response =
                new QualityAssessmentResponse(
                        DATA_COLLECTION_ID,
                        QUALITY_RUN_ID,
                        QualityLevel.GOOD,
                        true,
                        "Quality assessment completed successfully.",
                        OffsetDateTime.parse("2026-10-10T08:00:00+01:00"),
                        List.of(),
                        List.of());

        QualityAssessment assessment = mock(QualityAssessment.class);

        when(service.assess(any(DataCollectionId.class)))
                .thenReturn(assessment);

        when(mapper.toResponse(assessment))
                .thenReturn(response);

        mockMvc.perform(get(
                "/api/quality/data-collections/{dataCollectionId}/assessment",
                DATA_COLLECTION_ID))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataCollectionId")
                        .value(DATA_COLLECTION_ID.toString()))
                .andExpect(jsonPath("$.qualityRunId")
                        .value(QUALITY_RUN_ID.toString()))
                .andExpect(jsonPath("$.overallLevel")
                        .value("GOOD"))
                .andExpect(jsonPath("$.analysisComplete")
                        .value(true))
                .andExpect(jsonPath("$.summary")
                        .value("Quality assessment completed successfully."))
                .andExpect(jsonPath("$.dimensions").isArray())
                .andExpect(jsonPath("$.significantFindings").isArray());
    }
}
