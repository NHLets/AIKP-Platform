package org.afdb.aikp.modules.dashboard.presentation.controller;

import org.afdb.aikp.modules.dashboard.application.query.GetDashboardQuery;
import org.afdb.aikp.modules.dashboard.application.response.DashboardResponse;
import org.afdb.aikp.modules.dashboard.application.service.DashboardApplicationService;
import org.afdb.aikp.modules.dashboard.presentation.contract.DashboardApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController
        implements DashboardApi {

    private final DashboardApplicationService
            dashboardApplicationService;

    public DashboardController(
            DashboardApplicationService
                    dashboardApplicationService) {

        this.dashboardApplicationService =
                dashboardApplicationService;
    }

    @Override
    @GetMapping
    public ResponseEntity<DashboardResponse>
            getDashboard() {

        DashboardResponse response =
                dashboardApplicationService.handle(
                        new GetDashboardQuery());

        return ResponseEntity.ok(response);
    }
}
