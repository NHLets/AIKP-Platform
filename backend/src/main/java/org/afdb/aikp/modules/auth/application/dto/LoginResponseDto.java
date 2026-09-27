package org.afdb.aikp.modules.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT login response")
public record LoginResponseDto(

    @Schema(description = "Access token")
    String accessToken,

    @Schema(description = "Token type")
    String tokenType,

    @Schema(description = "User identifier")
    Long userId,

    @Schema(description = "User email")
    String email,

    @Schema(description = "Role")
    String role

) {}
