package org.afdb.aikp.modules.passwordreset.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDto(
    @Email @NotBlank String email
) {}
