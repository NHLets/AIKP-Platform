package org.afdb.aikp.modules.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dashboard KPI summary")
public record KpiSummaryDto(

    @Schema(description = "Total validation comments", example = "1248")
    Long totalComments,

    @Schema(description = "Critical validation comments", example = "42")
    Long criticalComments,

    @Schema(description = "Variables with comments", example = "387")
    Long affectedVariables,

    @Schema(description = "Questionnaires with comments", example = "18")
    Long affectedQuestionnaires

) {}
