package org.afdb.aikp.modules.validation.application.mapper;

import org.afdb.aikp.modules.validation.application.response.DataCollectionValidationResponse;
import org.afdb.aikp.modules.validation.application.response.DataCollectionValidationSummary;
import org.afdb.aikp.modules.validation.domain.model.DataCollectionValidation;

import org.springframework.stereotype.Component;

/**
 * Maps DataCollectionValidation domain objects to application responses.
 */
@Component
public class DataCollectionValidationApplicationMapper {

    /**
     * Maps a validation to its complete response.
     */
    public DataCollectionValidationResponse toResponse(
            DataCollectionValidation validation) {

        return new DataCollectionValidationResponse(
                validation
                        .getDataCollectionValidationId()
                        .getValue(),
                validation
                        .getDataCollectionId()
                        .getValue(),
                validation
                        .getValidatorId()
                        .getValue(),
                validation.getDecision(),
                validation.getComments() != null
                        ? validation.getComments().getValue()
                        : null,
                validation.getValidatedAt());
    }

    /**
     * Maps a validation to its summary representation.
     */
    public DataCollectionValidationSummary toSummary(
            DataCollectionValidation validation) {

        return new DataCollectionValidationSummary(
                validation
                        .getDataCollectionValidationId()
                        .getValue(),
                validation
                        .getValidatorId()
                        .getValue(),
                validation.getDecision(),
                validation.getValidatedAt());
    }
}
