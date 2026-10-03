package org.afdb.aikp.modules.dashboard.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.dashboard.domain.repository.DashboardAnalyticsRepository;
import org.afdb.aikp.modules.dashboard.dto.DashboardOverviewDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardAnalyticsApplicationServiceTest {

    @Mock
    private DashboardAnalyticsRepository dashboardAnalyticsRepository;

    private DashboardAnalyticsApplicationService service;

    private UUID campaignId;

    private static final Integer REFERENCE_YEAR = 2025;

    @BeforeEach
    void setUp() {
        service = new DashboardAnalyticsApplicationService(
                dashboardAnalyticsRepository);

        campaignId = UUID.randomUUID();
    }

    @Test
    void shouldBuildCompleteDashboardOverview() {

        when(dashboardAnalyticsRepository.findKpiSummary(
                campaignId,
                REFERENCE_YEAR))
                .thenReturn(new Object[] {
                        10L,
                        3L,
                        7L,
                        2L
                });

        when(dashboardAnalyticsRepository.findSeverityDistribution(
                campaignId,
                REFERENCE_YEAR))
                .thenReturn(List.of(
                        new Object[] {"CRITICAL", 3L},
                        new Object[] {"HIGH", 4L}
                ));

        when(dashboardAnalyticsRepository.findValidationTrend(
                campaignId,
                REFERENCE_YEAR))
                .thenReturn(List.of(
                        new Object[] {"2025-01", 5L, 1L},
                        new Object[] {"2025-02", 8L, 2L}
                ));

        when(dashboardAnalyticsRepository.findValidationHeatmap(
                campaignId,
                REFERENCE_YEAR))
                .thenReturn(List.of(
                        new Object[] {"PW_B_TEST", "B001", 4L},
                        new Object[] {"PW_B_TEST", "B002", 6L}
                ));

        DashboardOverviewDto result =
                service.getOverview(
                        campaignId,
                        REFERENCE_YEAR);

        assertNotNull(result);
        assertNotNull(result.kpiSummary());

        assertEquals(10L, result.kpiSummary().totalComments());
        assertEquals(3L, result.kpiSummary().criticalComments());
        assertEquals(7L, result.kpiSummary().affectedVariables());
        assertEquals(2L, result.kpiSummary().affectedQuestionnaires());

        assertEquals(2, result.severityDistribution().size());
        assertEquals(
                "CRITICAL",
                result.severityDistribution().get(0).severity());
        assertEquals(
                3L,
                result.severityDistribution().get(0).count());

        assertEquals(2, result.validationTrend().size());
        assertEquals(
                "2025-01",
                result.validationTrend().get(0).period());
        assertEquals(
                5L,
                result.validationTrend().get(0).validated());
        assertEquals(
                1L,
                result.validationTrend().get(0).rejected());

        assertEquals(2, result.validationHeatmap().size());
        assertEquals(
                "PW_B_TEST",
                result.validationHeatmap().get(0).questionnaire());
        assertEquals(
                "B001",
                result.validationHeatmap().get(0).variable());
        assertEquals(
                4L,
                result.validationHeatmap().get(0).count());

        verify(dashboardAnalyticsRepository)
                .findKpiSummary(
                        campaignId,
                        REFERENCE_YEAR);

        verify(dashboardAnalyticsRepository)
                .findSeverityDistribution(
                        campaignId,
                        REFERENCE_YEAR);

        verify(dashboardAnalyticsRepository)
                .findValidationTrend(
                        campaignId,
                        REFERENCE_YEAR);

        verify(dashboardAnalyticsRepository)
                .findValidationHeatmap(
                        campaignId,
                        REFERENCE_YEAR);
    }
}
