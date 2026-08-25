package org.afdb.aikp.modules.dashboard.presentation.contract;

import org.afdb.aikp.modules.dashboard.application.response.DashboardResponse;
import org.springframework.http.ResponseEntity;

public interface DashboardApi {

    ResponseEntity<DashboardResponse> getDashboard();
}
