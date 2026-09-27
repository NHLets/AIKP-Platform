package org.afdb.aikp.modules.validation.application.dto;

import org.afdb.aikp.modules.validation.domain.enums.ValidationSeverity;

import java.util.UUID;

public record CreateValidationCommentDto(

        UUID observationId,

        UUID validatorId,

        String comment,

        ValidationSeverity severity

) {
}
