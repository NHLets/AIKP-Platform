package org.afdb.aikp.modules.validation.application.dto;

import java.util.UUID;

public record RejectedObservationDto(
    UUID observationId,
    String questionnaireCode,
    String variableCode,
    String variableLabel,
    int referenceYear,
    String severity,
    String comment
) {
}
