package org.afdb.aikp.modules.iam.application.response;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(

        UUID id,

        String username,

        String email,

        String fullName,

        UserStatus status,

        Instant createdAt,

        Instant updatedAt,

        Instant lastLogin
) {
}