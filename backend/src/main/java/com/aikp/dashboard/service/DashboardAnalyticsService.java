package com.aikp.dashboard.service;

import com.aikp.dashboard.dto.SeverityDistributionDto;
import com.aikp.dashboard.repository.DashboardAnalyticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardAnalyticsService {

    private final DashboardAnalyticsRepository repository;

    public DashboardAnalyticsService(DashboardAnalyticsRepository repository) {
        this.repository = repository;
    }

    public List<SeverityDistributionDto> getSeverityDistribution(
            Long campaignId,
            Integer referenceYear) {

        return repository.findSeverityDistribution(campaignId, referenceYear)
                .stream()
                .map(row -> new SeverityDistributionDto(
                        (String) row[0],
                        ((Number) row[1]).longValue()
                ))
                .toList();
    }

    public List<ValidationTrendDto> getValidationTrend(
            Long campaignId,
            Integer referenceYear) {

        return validationCommentJpaRepository
                .findValidationTrend(campaignId, referenceYear)
                .stream()
                .map(row -> new ValidationTrendDto(
                        ((Number) row[0]).intValue(),
                        (String) row[1],
                        ((Number) row[2]).longValue()
                ))
                .toList();
    }


    public List<ValidationHeatmapDto> getValidationHeatmap(
            Long campaignId,
            Integer referenceYear) {

        return validationCommentJpaRepository
                .findValidationHeatmap(campaignId, referenceYear)
                .stream()
                .map(row -> new ValidationHeatmapDto(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).longValue()
                ))
                .toList();
    }


    public KpiSummaryDto getKpiSummary(
            Long campaignId,
            Integer referenceYear) {

        Object[] row = validationCommentJpaRepository
                .findKpiSummary(campaignId, referenceYear);

        return new KpiSummaryDto(
                ((Number) row[0]).longValue(),
                ((Number) row[1]).longValue(),
                ((Number) row[2]).longValue(),
                ((Number) row[3]).longValue()
        );
    }


    public DashboardOverviewDto getDashboardOverview(
            Long campaignId,
            Integer referenceYear) {

        return new DashboardOverviewDto(
                getKpiSummary(campaignId, referenceYear),
                getSeverityDistribution(campaignId, referenceYear),
                getValidationTrend(campaignId, referenceYear),
                getValidationHeatmap(campaignId, referenceYear)
        );
    }

}