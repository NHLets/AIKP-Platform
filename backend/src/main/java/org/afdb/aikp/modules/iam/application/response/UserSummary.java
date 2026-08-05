package org.afdb.aikp.modules.iam.application.response;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;

import java.util.UUID;

public record UserSummary(

        UUID id,

        String username,

        String fullName,

        String email,

        UserStatus status
) {
}