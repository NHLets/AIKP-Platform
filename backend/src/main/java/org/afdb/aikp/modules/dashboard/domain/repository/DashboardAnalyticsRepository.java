package org.afdb.aikp.modules.dashboard.domain.repository;

import java.util.List;
import java.util.UUID;

public interface DashboardAnalyticsRepository {

    Object[] findKpiSummary(
            UUID campaignId,
            Integer referenceYear);

    List<Object[]> findSeverityDistribution(
            UUID campaignId,
            Integer referenceYear);

    List<Object[]> findValidationTrend(
            UUID campaignId,
            Integer referenceYear);

    List<Object[]> findValidationHeatmap(
            UUID campaignId,
            Integer referenceYear);
}
