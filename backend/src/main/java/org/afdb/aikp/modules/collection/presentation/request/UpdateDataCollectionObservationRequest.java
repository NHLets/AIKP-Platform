package org.afdb.aikp.modules.collection.presentation.request;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * HTTP request for updating a data collection observation.
 */
public record UpdateDataCollectionObservationRequest(
        BigDecimal numericValue,
        String textValue,
        Boolean booleanValue,
        LocalDate dateValue,
        String selectedUnit,
        String comment) {
}
