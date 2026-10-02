package org.afdb.aikp.modules.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Monthly validation trend")
public record ValidationTrendDto(

    @Schema(example = "2026-01")
    String period,

    @Schema(example = "128")
    Long validated,

    @Schema(example = "17")
    Long rejected

) {}
