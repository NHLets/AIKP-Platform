package org.afdb.aikp.modules.validation.domain.repository;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.validation.domain.model.DataCollectionValidation;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;

/**
 * Repository contract for DataCollectionValidation aggregates.
 */
public interface DataCollectionValidationRepository {

    /**
     * Persists a data collection validation.
     */
    DataCollectionValidation save(
            DataCollectionValidation validation);

    /**
     * Finds a validation by its identifier.
     */
    Optional<DataCollectionValidation> findById(
            DataCollectionValidationId id);

    /**
     * Returns all validations for a data collection.
     */
    List<DataCollectionValidation> findByDataCollectionId(
            DataCollectionId dataCollectionId);

    /**
     * Checks whether a validation exists by its identifier.
     */
    boolean existsById(
            DataCollectionValidationId id);

    /**
     * Deletes a validation.
     */
    void delete(
            DataCollectionValidation validation);
}
