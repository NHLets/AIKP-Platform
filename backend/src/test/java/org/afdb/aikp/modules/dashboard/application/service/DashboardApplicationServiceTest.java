package org.afdb.aikp.modules.dashboard.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.afdb.aikp.modules.dashboard.application.query.GetDashboardQuery;
import org.afdb.aikp.modules.dashboard.application.response.DashboardResponse;
import org.afdb.aikp.modules.dashboard.domain.repository.DashboardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardApplicationServiceTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private DashboardApplicationService dashboardApplicationService;

    @Test
    void shouldGetDashboard() {

        DashboardResponse expectedResponse =
                new DashboardResponse(
                        10,
                        5,
                        20,
                        30,
                        4,
                        8,
                        6,
                        10,
                        2,
                        100,
                        80);

        when(dashboardRepository.getDashboard())
                .thenReturn(expectedResponse);

        DashboardResponse actualResponse =
                dashboardApplicationService.handle(
                        new GetDashboardQuery());

        assertThat(actualResponse)
                .isEqualTo(expectedResponse);

        verify(dashboardRepository)
                .getDashboard();
    }
}
