package org.afdb.aikp.modules.validation.infrastructure.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;
import org.afdb.aikp.modules.validation.domain.model.DataCollectionValidation;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;
import org.afdb.aikp.modules.validation.domain.valueobject.ValidationComments;
import org.afdb.aikp.modules.validation.infrastructure.persistence.entity.DataCollectionValidationEntity;

import org.junit.jupiter.api.Test;

class DataCollectionValidationPersistenceMapperTest {

    private final DataCollectionValidationPersistenceMapper mapper =
            new DataCollectionValidationPersistenceMapper();

    @Test
    void shouldMapDomainToEntity() {

        DataCollectionValidationId validationId =
                DataCollectionValidationId.generate();

        DataCollectionId dataCollectionId =
                DataCollectionId.generate();

        PersonId validatorId =
                PersonId.generate();

        Instant validatedAt =
                Instant.parse("2026-08-23T12:00:00Z");

        DataCollectionValidation validation =
                DataCollectionValidation.create(
                        validationId,
                        dataCollectionId,
                        validatorId,
                        ValidationDecision.VALIDATED,
                        ValidationComments.of(
                                "Validation successful."),
                        validatedAt);

        DataCollectionValidationEntity entity =
                mapper.toEntity(validation);

        assertThat(entity.getId())
                .isEqualTo(validationId.getValue());

        assertThat(entity.getDataCollectionId())
                .isEqualTo(
                        dataCollectionId.getValue());

        assertThat(entity.getValidatorId())
                .isEqualTo(
                        validatorId.getValue());

        assertThat(entity.getDecision())
                .isEqualTo(
                        ValidationDecision.VALIDATED);

        assertThat(entity.getComments())
                .isEqualTo(
                        "Validation successful.");

        assertThat(entity.getValidatedAt())
                .isEqualTo(validatedAt);
    }

    @Test
    void shouldMapEntityToDomain() {

        UUID validationId =
                UUID.randomUUID();

        UUID dataCollectionId =
                UUID.randomUUID();

        UUID validatorId =
                UUID.randomUUID();

        Instant validatedAt =
                Instant.parse("2026-08-23T12:00:00Z");

        DataCollectionValidationEntity entity =
                new DataCollectionValidationEntity(
                        validationId,
                        dataCollectionId,
                        validatorId,
                        ValidationDecision.REJECTED,
                        "Missing required information.",
                        validatedAt);

        DataCollectionValidation validation =
                mapper.toDomain(entity);

        assertThat(
                validation.getDataCollectionValidationId()
                        .getValue())
                .isEqualTo(validationId);

        assertThat(
                validation.getDataCollectionId()
                        .getValue())
                .isEqualTo(dataCollectionId);

        assertThat(
                validation.getValidatorId()
                        .getValue())
                .isEqualTo(validatorId);

        assertThat(validation.getDecision())
                .isEqualTo(
                        ValidationDecision.REJECTED);

        assertThat(validation.getComments())
                .isNotNull();

        assertThat(
                validation.getComments()
                        .getValue())
                .isEqualTo(
                        "Missing required information.");

        assertThat(validation.getValidatedAt())
                .isEqualTo(validatedAt);
    }

    @Test
    void shouldMapNullCommentsFromDomainToEntity() {

        DataCollectionValidation validation =
                DataCollectionValidation.create(
                        DataCollectionValidationId.generate(),
                        DataCollectionId.generate(),
                        PersonId.generate(),
                        ValidationDecision.VALIDATED,
                        null,
                        Instant.parse(
                                "2026-08-23T12:00:00Z"));

        DataCollectionValidationEntity entity =
                mapper.toEntity(validation);

        assertThat(entity.getComments())
                .isNull();
    }

    @Test
    void shouldMapNullCommentsFromEntityToDomain() {

        DataCollectionValidationEntity entity =
                new DataCollectionValidationEntity(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        ValidationDecision.VALIDATED,
                        null,
                        Instant.parse(
                                "2026-08-23T12:00:00Z"));

        DataCollectionValidation validation =
                mapper.toDomain(entity);

        assertThat(validation.getComments())
                .isNull();
    }
}
