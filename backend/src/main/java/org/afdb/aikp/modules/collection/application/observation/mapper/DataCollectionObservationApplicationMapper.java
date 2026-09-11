package org.afdb.aikp.modules.collection.application.observation.mapper;

import org.afdb.aikp.modules.collection.application.observation.response.DataCollectionObservationResponse;
import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.springframework.stereotype.Component;

/**
 * Maps DataCollectionObservation domain objects
 * to application responses.
 */
@Component
public class DataCollectionObservationApplicationMapper {

    public DataCollectionObservationResponse toResponse(
            DataCollectionObservation observation) {

        return new DataCollectionObservationResponse(
                observation.getId().getValue(),
                observation.getDataCollectionId().getValue(),
                observation.getQuestionnaireVariableId().getValue(),
                observation.getReferenceYear(),
                observation.getStatus(),
                observation.getNumericValue(),
                observation.getTextValue(),
                observation.getBooleanValue(),
                observation.getDateValue(),
                observation.getSelectedUnit(),
                observation.getComment());
    }
}
