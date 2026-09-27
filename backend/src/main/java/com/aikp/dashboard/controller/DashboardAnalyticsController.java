package com.aikp.dashboard.controller;

import com.aikp.dashboard.dto.SeverityDistributionDto;
import com.aikp.dashboard.service.DashboardAnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/analytics")
public class DashboardAnalyticsController {

    private final DashboardAnalyticsService service;

    public DashboardAnalyticsController(DashboardAnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/severity-distribution")
    public List<SeverityDistributionDto> getSeverityDistribution(
            @RequestParam Long campaignId,
            @RequestParam Integer referenceYear) {

        return service.getSeverityDistribution(campaignId, referenceYear);
    }

    @GetMapping("/validation-trend")
    public List<ValidationTrendDto> getValidationTrend(
            @RequestParam Long campaignId,
            @RequestParam Integer referenceYear) {

        return service.getValidationTrend(campaignId, referenceYear);
    }


    @GetMapping("/validation-heatmap")
    public List<ValidationHeatmapDto> getValidationHeatmap(
            @RequestParam Long campaignId,
            @RequestParam Integer referenceYear) {

        return service.getValidationHeatmap(campaignId, referenceYear);
    }


    @GetMapping("/kpi-summary")
    public KpiSummaryDto getKpiSummary(
            @RequestParam Long campaignId,
            @RequestParam Integer referenceYear) {

        return service.getKpiSummary(campaignId, referenceYear);
    }


    @GetMapping("/overview")
    public DashboardOverviewDto getDashboardOverview(
            @RequestParam Long campaignId,
            @RequestParam Integer referenceYear) {

        return service.getDashboardOverview(campaignId, referenceYear);
    }

}