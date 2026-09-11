package org.afdb.aikp.modules.collection.presentation.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * HTTP request for creating a data collection observation.
 */
public record CreateDataCollectionObservationRequest(
        UUID dataCollectionId,
        UUID questionnaireVariableId,
        int referenceYear,
        BigDecimal numericValue,
        String textValue,
        Boolean booleanValue,
        LocalDate dateValue,
        String selectedUnit,
        String comment) {
}
