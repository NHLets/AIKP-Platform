package org.afdb.aikp.modules.invitation.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateInvitationRequestDto(
    @NotNull UUID userId
) {}
