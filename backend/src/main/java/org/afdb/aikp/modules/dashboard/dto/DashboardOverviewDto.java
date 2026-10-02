package org.afdb.aikp.modules.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.afdb.aikp.modules.dashboard.dto.SeverityDistributionDto;
import org.afdb.aikp.modules.dashboard.dto.ValidationTrendDto;

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
