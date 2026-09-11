package org.afdb.aikp.modules.collection.domain.repository;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;

/**
 * Repository abstraction for DataCollectionObservation aggregates.
 */
public interface DataCollectionObservationRepository {

    DataCollectionObservation save(
            DataCollectionObservation observation);

    Optional<DataCollectionObservation> findById(
            DataCollectionObservationId id);

    List<DataCollectionObservation> findByDataCollectionId(
            DataCollectionId dataCollectionId);

    List<DataCollectionObservation> findByQuestionnaireVariableId(
            QuestionnaireVariableId questionnaireVariableId);

    Optional<DataCollectionObservation> findByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
            DataCollectionId dataCollectionId,
            QuestionnaireVariableId questionnaireVariableId,
            int referenceYear);

    boolean existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
            DataCollectionId dataCollectionId,
            QuestionnaireVariableId questionnaireVariableId,
            int referenceYear);

    void delete(
            DataCollectionObservation observation);
}
