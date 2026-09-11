package org.afdb.aikp.modules.collection.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionObservationEntity;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;
import org.springframework.stereotype.Component;

/**
 * Maps DataCollectionObservation domain objects to persistence
 * entities and persistence entities back to domain objects.
 *
 * <p>
 * The questionnaire variable data type is deliberately not
 * persisted in the observation table. It is supplied when
 * reconstructing the domain object from the questionnaire
 * variable definition.
 * </p>
 */
@Component
public class DataCollectionObservationPersistenceMapper {

    public DataCollectionObservationEntity toEntity(
            DataCollectionObservation observation) {

        return new DataCollectionObservationEntity(
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

    public DataCollectionObservation toDomain(
            DataCollectionObservationEntity entity,
            QuestionnaireVariableDataType dataType) {

        return DataCollectionObservation.restore(
                DataCollectionObservationId.of(
                        entity.getId()),
                DataCollectionId.of(
                        entity.getDataCollectionId()),
                QuestionnaireVariableId.of(
                        entity.getQuestionnaireVariableId()),
                entity.getReferenceYear(),
                dataType,
                entity.getStatus(),
                entity.getNumericValue(),
                entity.getTextValue(),
                entity.getBooleanValue(),
                entity.getDateValue(),
                entity.getSelectedUnit(),
                entity.getComment());
    }
}
