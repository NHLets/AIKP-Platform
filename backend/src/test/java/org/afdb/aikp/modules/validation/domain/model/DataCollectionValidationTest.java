package org.afdb.aikp.modules.validation.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;
import org.afdb.aikp.modules.validation.domain.valueobject.ValidationComments;

import org.junit.jupiter.api.Test;

class DataCollectionValidationTest {

    @Test
    void shouldCreateValidation() {

        DataCollectionValidationId id =
                DataCollectionValidationId.generate();

        DataCollectionId dataCollectionId =
                DataCollectionId.generate();

        PersonId validatorId =
                PersonId.generate();

        ValidationComments comments =
                ValidationComments.of(
                        "Data collection has been reviewed.");

        Instant validatedAt =
                Instant.now();

        DataCollectionValidation validation =
                DataCollectionValidation.create(
                        id,
                        dataCollectionId,
                        validatorId,
                        ValidationDecision.VALIDATED,
                        comments,
                        validatedAt);

        assertThat(
                validation.getDataCollectionValidationId())
                .isEqualTo(id);

        assertThat(
                validation.getDataCollectionId())
                .isEqualTo(dataCollectionId);

        assertThat(
                validation.getValidatorId())
                .isEqualTo(validatorId);

        assertThat(
                validation.getDecision())
                .isEqualTo(
                        ValidationDecision.VALIDATED);

        assertThat(
                validation.getComments())
                .isEqualTo(comments);

        assertThat(
                validation.getValidatedAt())
                .isEqualTo(validatedAt);
    }

    @Test
    void shouldCreateValidationWithoutComments() {

        DataCollectionValidation validation =
                DataCollectionValidation.create(
                        DataCollectionValidationId.generate(),
                        DataCollectionId.generate(),
                        PersonId.generate(),
                        ValidationDecision.REJECTED,
                        null,
                        Instant.now());

        assertThat(validation.getComments())
                .isNull();

        assertThat(validation.getDecision())
                .isEqualTo(
                        ValidationDecision.REJECTED);
    }

    @Test
    void shouldRestoreValidation() {

        DataCollectionValidationId id =
                DataCollectionValidationId.generate();

        DataCollectionId dataCollectionId =
                DataCollectionId.generate();

        PersonId validatorId =
                PersonId.generate();

        ValidationComments comments =
                ValidationComments.of(
                        "Restored validation.");

        Instant validatedAt =
                Instant.now();

        DataCollectionValidation validation =
                DataCollectionValidation.restore(
                        id,
                        dataCollectionId,
                        validatorId,
                        ValidationDecision.VALIDATED,
                        comments,
                        validatedAt);

        assertThat(
                validation.getDataCollectionValidationId())
                .isEqualTo(id);

        assertThat(
                validation.getDataCollectionId())
                .isEqualTo(dataCollectionId);

        assertThat(
                validation.getValidatorId())
                .isEqualTo(validatorId);

        assertThat(
                validation.getDecision())
                .isEqualTo(
                        ValidationDecision.VALIDATED);

        assertThat(
                validation.getComments())
                .isEqualTo(comments);

        assertThat(
                validation.getValidatedAt())
                .isEqualTo(validatedAt);
    }

    @Test
    void shouldRejectNullId() {

        assertThatThrownBy(() ->
                DataCollectionValidation.create(
                        null,
                        DataCollectionId.generate(),
                        PersonId.generate(),
                        ValidationDecision.VALIDATED,
                        null,
                        Instant.now()))
                .isInstanceOf(
                        NullPointerException.class)
                .hasMessage(
                        "Data collection validation ID "
                                + "cannot be null.");
    }

    @Test
    void shouldRejectNullDataCollectionId() {

        assertThatThrownBy(() ->
                DataCollectionValidation.create(
                        DataCollectionValidationId.generate(),
                        null,
                        PersonId.generate(),
                        ValidationDecision.VALIDATED,
                        null,
                        Instant.now()))
                .isInstanceOf(
                        NullPointerException.class)
                .hasMessage(
                        "Data collection ID cannot be null.");
    }

    @Test
    void shouldRejectNullValidatorId() {

        assertThatThrownBy(() ->
                DataCollectionValidation.create(
                        DataCollectionValidationId.generate(),
                        DataCollectionId.generate(),
                        null,
                        ValidationDecision.VALIDATED,
                        null,
                        Instant.now()))
                .isInstanceOf(
                        NullPointerException.class)
                .hasMessage(
                        "Validator ID cannot be null.");
    }

    @Test
    void shouldRejectNullDecision() {

        assertThatThrownBy(() ->
                DataCollectionValidation.create(
                        DataCollectionValidationId.generate(),
                        DataCollectionId.generate(),
                        PersonId.generate(),
                        null,
                        null,
                        Instant.now()))
                .isInstanceOf(
                        NullPointerException.class)
                .hasMessage(
                        "Validation decision cannot be null.");
    }

    @Test
    void shouldRejectNullValidatedAt() {

        assertThatThrownBy(() ->
                DataCollectionValidation.create(
                        DataCollectionValidationId.generate(),
                        DataCollectionId.generate(),
                        PersonId.generate(),
                        ValidationDecision.VALIDATED,
                        null,
                        null))
                .isInstanceOf(
                        NullPointerException.class)
                .hasMessage(
                        "Validation timestamp cannot be null.");
    }
}
