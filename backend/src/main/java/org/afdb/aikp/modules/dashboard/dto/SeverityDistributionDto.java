package org.afdb.aikp.modules.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Distribution of validation severity")
public record SeverityDistributionDto(

    @Schema(example = "CRITICAL")
    String severity,

    @Schema(example = "42")
    Long count

) {}
