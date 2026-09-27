package com.aikp.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Distribution of validation comments by severity level")
public record SeverityDistributionDto(

    @Schema(description = "Severity level", example = "ERROR")
    String severity,

    @Schema(description = "Number of comments", example = "42")
    Long count

) {}
