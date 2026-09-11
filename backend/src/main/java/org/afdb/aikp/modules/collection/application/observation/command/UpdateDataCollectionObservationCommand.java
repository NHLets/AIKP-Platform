package org.afdb.aikp.modules.collection.application.observation.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Command for updating a data collection observation.
 */
public record UpdateDataCollectionObservationCommand(
        UUID dataCollectionObservationId,
        BigDecimal numericValue,
        String textValue,
        Boolean booleanValue,
        LocalDate dateValue,
        String selectedUnit,
        String comment) {
}
