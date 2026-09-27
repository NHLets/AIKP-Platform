package org.afdb.aikp.modules.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validation heatmap cell")
public record ValidationHeatmapDto(

    @Schema(description = "Questionnaire code", example = "POWER-G")
    String questionnaire,

    @Schema(description = "Variable code", example = "GEN_001")
    String variable,

    @Schema(description = "Number of validation comments", example = "17")
    Long count

) {}
