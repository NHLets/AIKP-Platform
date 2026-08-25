package org.afdb.aikp.modules.validation.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.modules.validation.domain.model.DataCollectionValidation;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;
import org.afdb.aikp.modules.validation.domain.valueobject.ValidationComments;
import org.afdb.aikp.modules.validation.infrastructure.persistence.entity.DataCollectionValidationEntity;

import org.springframework.stereotype.Component;

/**
 * Maps DataCollectionValidation domain aggregates to persistence
 * entities and persistence entities back to domain aggregates.
 */
@Component
public class DataCollectionValidationPersistenceMapper {

    public DataCollectionValidationEntity toEntity(
            DataCollectionValidation validation) {

        String comments =
                validation.getComments() == null
                        ? null
                        : validation
                                .getComments()
                                .getValue();

        return new DataCollectionValidationEntity(
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
                comments,
                validation.getValidatedAt());
    }

    public DataCollectionValidation toDomain(
            DataCollectionValidationEntity entity) {

        ValidationComments comments =
                entity.getComments() == null
                        ? null
                        : ValidationComments.of(
                                entity.getComments());

        return DataCollectionValidation.restore(
                DataCollectionValidationId.of(
                        entity.getId()),
                DataCollectionId.of(
                        entity.getDataCollectionId()),
                PersonId.of(
                        entity.getValidatorId()),
                entity.getDecision(),
                comments,
                entity.getValidatedAt());
    }
}
