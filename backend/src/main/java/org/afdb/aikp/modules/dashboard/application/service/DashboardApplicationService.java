package org.afdb.aikp.modules.dashboard.application.service;

import org.afdb.aikp.modules.dashboard.application.query.GetDashboardQuery;
import org.afdb.aikp.modules.dashboard.application.response.DashboardResponse;
import org.afdb.aikp.modules.dashboard.domain.repository.DashboardRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardApplicationService {

    private final DashboardRepository dashboardRepository;

    public DashboardApplicationService(
            DashboardRepository dashboardRepository) {

        this.dashboardRepository =
                dashboardRepository;
    }

    public DashboardResponse handle(
            GetDashboardQuery query) {

        return dashboardRepository
                .getDashboard();
    }
}
