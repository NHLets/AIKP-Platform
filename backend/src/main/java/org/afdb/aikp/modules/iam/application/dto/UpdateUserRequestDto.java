package org.afdb.aikp.modules.iam.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDto(
    @NotBlank String username,
    @Email String email,
    @NotBlank String fullName
) {}
