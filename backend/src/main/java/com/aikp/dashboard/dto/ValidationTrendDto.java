package com.aikp.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Monthly validation trend")
public record ValidationTrendDto(

    @Schema(description = "Month (1-12)", example = "9")
    Integer month,

    @Schema(description = "Month name", example = "Sep")
    String monthName,

    @Schema(description = "Number of validation comments", example = "128")
    Long count

) {}
