package org.afdb.aikp.modules.dashboard.presentation.controller;

import java.util.UUID;

import org.afdb.aikp.modules.dashboard.application.service.DashboardAnalyticsApplicationService;
import org.afdb.aikp.modules.dashboard.dto.DashboardOverviewDto;
import org.afdb.aikp.modules.dashboard.presentation.contract.DashboardAnalyticsApi;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','COORDINATOR','VALIDATOR','DATA_PROVIDER')")
@RequestMapping("/api/dashboard/analytics")
public class DashboardAnalyticsController
        implements DashboardAnalyticsApi {

    private final DashboardAnalyticsApplicationService
            dashboardAnalyticsApplicationService;

    public DashboardAnalyticsController(
            DashboardAnalyticsApplicationService
                    dashboardAnalyticsApplicationService) {

        this.dashboardAnalyticsApplicationService =
                dashboardAnalyticsApplicationService;
    }

    @Override
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDto> getOverview(
            @RequestParam("campaignId") UUID campaignId,
            @RequestParam("referenceYear") Integer referenceYear) {

        DashboardOverviewDto response =
                dashboardAnalyticsApplicationService.getOverview(
                        campaignId,
                        referenceYear);

        return ResponseEntity.ok(response);
    }
}
