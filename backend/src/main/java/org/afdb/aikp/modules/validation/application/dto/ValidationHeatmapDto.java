package org.afdb.aikp.modules.validation.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validation heatmap cell")
public record ValidationHeatmapDto(

    @Schema(description = "Questionnaire code", example = "POWER-G")
    String questionnaireCode,

    @Schema(description = "Variable code", example = "GEN_001")
    String variableCode,

    @Schema(description = "Validation status")
    Integer validationStatus,

    @Schema(description = "Number of observations", example = "17")
    Long count

) {}
