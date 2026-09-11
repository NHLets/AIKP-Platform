package org.afdb.aikp.modules.collection.domain.exception;

import java.util.UUID;

import org.afdb.aikp.shared.exception.NotFoundException;

/**
 * Thrown when an observation cannot be found by its
 * DataCollection, questionnaire variable and reference year.
 */
public class DataCollectionObservationNotFoundByKeyException
        extends NotFoundException {

    public DataCollectionObservationNotFoundByKeyException(
            UUID dataCollectionId,
            UUID questionnaireVariableId,
            int referenceYear) {

        super(
                "Data collection observation not found for data collection "
                        + dataCollectionId
                        + ", questionnaire variable "
                        + questionnaireVariableId
                        + " and reference year "
                        + referenceYear);
    }
}
