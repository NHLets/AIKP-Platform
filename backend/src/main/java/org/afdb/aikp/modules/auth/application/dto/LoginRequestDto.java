package org.afdb.aikp.modules.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request")
public record LoginRequestDto(

    @Email
    @NotBlank
    @Schema(description = "User email")
    String email,

    @NotBlank
    @Schema(description = "User password")
    String password

) {}
