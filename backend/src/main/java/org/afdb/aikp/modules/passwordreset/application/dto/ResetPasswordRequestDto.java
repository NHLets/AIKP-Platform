package org.afdb.aikp.modules.passwordreset.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDto(
    @NotBlank String token,
    @NotBlank String password
) {}
