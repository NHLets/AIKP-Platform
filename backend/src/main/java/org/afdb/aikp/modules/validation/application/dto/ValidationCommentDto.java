package org.afdb.aikp.modules.validation.application.dto;

import org.afdb.aikp.modules.validation.domain.enums.ValidationSeverity;

import java.time.Instant;
import java.util.UUID;

public record ValidationCommentDto(

        UUID id,

        UUID observationId,

        UUID validatorId,

        String comment,

        ValidationSeverity severity,

        Instant createdAt,

        Instant updatedAt

) {
}
