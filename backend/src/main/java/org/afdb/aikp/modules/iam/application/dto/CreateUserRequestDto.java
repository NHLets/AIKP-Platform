package org.afdb.aikp.modules.iam.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateUserRequestDto(
    @NotBlank String username,
    @Email String email,
    @NotBlank String fullName,
    @NotBlank String password,
    @NotNull UUID organizationId,
    @NotNull UUID roleId
) {}
