package org.afdb.aikp.modules.iam.application.dto;

import java.util.UUID;

public record UserSummaryDto(
    UUID id,
    String username,
    String email,
    String fullName,
    String role,
    String organization,
    String status
) {}
