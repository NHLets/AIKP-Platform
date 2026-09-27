package org.afdb.aikp.modules.validation.application.dto;

import java.util.UUID;

public record ObservationCommentCountDto(
    UUID observationId,
    long count
) {
}
