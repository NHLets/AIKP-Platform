package org.afdb.aikp.modules.iam.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AssignRoleRequestDto(
    @NotNull UUID roleId
) {}
