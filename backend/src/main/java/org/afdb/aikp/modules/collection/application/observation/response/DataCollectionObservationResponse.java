package org.afdb.aikp.modules.collection.application.observation.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;

/**
 * Application response representing a data collection observation.
 */
public record DataCollectionObservationResponse(
        UUID id,
        UUID dataCollectionId,
        UUID questionnaireVariableId,
        int referenceYear,
        ObservationStatus status,
        BigDecimal numericValue,
        String textValue,
        Boolean booleanValue,
        LocalDate dateValue,
        String selectedUnit,
        String comment) {
}
