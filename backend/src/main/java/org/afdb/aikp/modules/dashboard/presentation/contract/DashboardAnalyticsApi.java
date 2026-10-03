package org.afdb.aikp.modules.dashboard.presentation.contract;

import java.util.UUID;

import org.afdb.aikp.modules.dashboard.dto.DashboardOverviewDto;
import org.springframework.http.ResponseEntity;

public interface DashboardAnalyticsApi {

    ResponseEntity<DashboardOverviewDto> getOverview(
            UUID campaignId,
            Integer referenceYear);
}
