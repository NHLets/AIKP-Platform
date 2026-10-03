package org.afdb.aikp.modules.dashboard.application.service;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.dashboard.domain.repository.DashboardAnalyticsRepository;
import org.afdb.aikp.modules.dashboard.dto.DashboardOverviewDto;
import org.afdb.aikp.modules.dashboard.dto.KpiSummaryDto;
import org.afdb.aikp.modules.dashboard.dto.SeverityDistributionDto;
import org.afdb.aikp.modules.dashboard.dto.ValidationHeatmapDto;
import org.afdb.aikp.modules.dashboard.dto.ValidationTrendDto;
import org.springframework.stereotype.Service;

@Service
public class DashboardAnalyticsApplicationService {

    private final DashboardAnalyticsRepository dashboardAnalyticsRepository;

    public DashboardAnalyticsApplicationService(
            DashboardAnalyticsRepository dashboardAnalyticsRepository) {

        this.dashboardAnalyticsRepository =
                dashboardAnalyticsRepository;
    }

    public DashboardOverviewDto getOverview(
            UUID campaignId,
            Integer referenceYear) {

        Object[] kpiResult =
                dashboardAnalyticsRepository.findKpiSummary(
                        campaignId,
                        referenceYear);

        KpiSummaryDto kpiSummary =
                new KpiSummaryDto(
                        toLong(kpiResult[0]),
                        toLong(kpiResult[1]),
                        toLong(kpiResult[2]),
                        toLong(kpiResult[3]));

        List<SeverityDistributionDto> severityDistribution =
                dashboardAnalyticsRepository
                        .findSeverityDistribution(
                                campaignId,
                                referenceYear)
                        .stream()
                        .map(row -> new SeverityDistributionDto(
                                row[0] != null ? row[0].toString() : null,
                                toLong(row[1])))
                        .toList();

        List<ValidationTrendDto> validationTrend =
                dashboardAnalyticsRepository
                        .findValidationTrend(
                                campaignId,
                                referenceYear)
                        .stream()
                        .map(row -> new ValidationTrendDto(
                                row[0] != null ? row[0].toString() : null,
                                toLong(row[1]),
                                toLong(row[2])))
                        .toList();

        List<ValidationHeatmapDto> validationHeatmap =
                dashboardAnalyticsRepository
                        .findValidationHeatmap(
                                campaignId,
                                referenceYear)
                        .stream()
                        .map(row -> new ValidationHeatmapDto(
                                row[0] != null ? row[0].toString() : null,
                                row[1] != null ? row[1].toString() : null,
                                toLong(row[2])))
                        .toList();

        return new DashboardOverviewDto(
                kpiSummary,
                severityDistribution,
                validationTrend,
                validationHeatmap);
    }

    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }

        if (value instanceof Number number) {
            return number.longValue();
        }

        return Long.valueOf(value.toString());
    }
}
