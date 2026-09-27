package org.afdb.aikp.modules.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Complete dashboard overview")
public record DashboardOverviewDto(

    @Schema(description = "Dashboard KPI summary")
    KpiSummaryDto kpiSummary,

    @Schema(description = "Distribution by severity")
    List<SeverityDistributionDto> severityDistribution,

    @Schema(description = "Monthly validation trend")
    List<ValidationTrendDto> validationTrend,

    @Schema(description = "Validation heatmap")
    List<ValidationHeatmapDto> validationHeatmap

) {}
