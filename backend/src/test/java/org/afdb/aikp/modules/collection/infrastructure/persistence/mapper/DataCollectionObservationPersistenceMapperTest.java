package org.afdb.aikp.modules.collection.infrastructure.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;
import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionObservationEntity;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;

import org.junit.jupiter.api.Test;

class DataCollectionObservationPersistenceMapperTest {

    private final DataCollectionObservationPersistenceMapper mapper =
            new DataCollectionObservationPersistenceMapper();

    @Test
    void shouldMapDomainToEntity() {

        DataCollectionId dataCollectionId =
                DataCollectionId.generate();

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.generate();

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        dataCollectionId,
                        variableId,
                        2025,
                        QuestionnaireVariableDataType.DECIMAL,
                        new BigDecimal("123.45"),
                        null,
                        null,
                        null,
                        "USD",
                        "Test observation.");

        DataCollectionObservationEntity entity =
                mapper.toEntity(observation);

        assertThat(entity.getId())
                .isEqualTo(
                        observation.getId().getValue());

        assertThat(entity.getDataCollectionId())
                .isEqualTo(
                        dataCollectionId.getValue());

        assertThat(entity.getQuestionnaireVariableId())
                .isEqualTo(
                        variableId.getValue());

        assertThat(entity.getReferenceYear())
                .isEqualTo(2025);

        assertThat(entity.getStatus())
                .isEqualTo(ObservationStatus.PROVIDED);

        assertThat(entity.getNumericValue())
                .isEqualByComparingTo("123.45");

        assertThat(entity.getTextValue())
                .isNull();

        assertThat(entity.getBooleanValue())
                .isNull();

        assertThat(entity.getDateValue())
                .isNull();

        assertThat(entity.getSelectedUnit())
                .isEqualTo("USD");

        assertThat(entity.getComment())
                .isEqualTo("Test observation.");
    }

    @Test
    void shouldMapEntityToDomain() {

        DataCollectionObservationId observationId =
                DataCollectionObservationId.generate();

        DataCollectionId dataCollectionId =
                DataCollectionId.generate();

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.generate();

        DataCollectionObservationEntity entity =
                new DataCollectionObservationEntity(
                        observationId.getValue(),
                        dataCollectionId.getValue(),
                        variableId.getValue(),
                        2025,
                        ObservationStatus.PROVIDED,
                        new BigDecimal("987.65"),
                        null,
                        null,
                        null,
                        "USD",
                        "Restored observation.");

        DataCollectionObservation observation =
                mapper.toDomain(
                        entity,
                        QuestionnaireVariableDataType.DECIMAL);

        assertThat(observation.getId())
                .isEqualTo(observationId);

        assertThat(observation.getDataCollectionId())
                .isEqualTo(dataCollectionId);

        assertThat(observation.getQuestionnaireVariableId())
                .isEqualTo(variableId);

        assertThat(observation.getReferenceYear())
                .isEqualTo(2025);

        assertThat(observation.getStatus())
                .isEqualTo(ObservationStatus.PROVIDED);

        assertThat(observation.getNumericValue())
                .isEqualByComparingTo("987.65");

        assertThat(observation.getSelectedUnit())
                .isEqualTo("USD");

        assertThat(observation.getComment())
                .isEqualTo("Restored observation.");
    }

    @Test
    void shouldMapNotAvailableObservation() {

        DataCollectionObservation observation =
                DataCollectionObservation.notAvailable(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2020,
                        QuestionnaireVariableDataType.DECIMAL,
                        "USD",
                        "Data unavailable.");

        DataCollectionObservationEntity entity =
                mapper.toEntity(observation);

        assertThat(entity.getStatus())
                .isEqualTo(
                        ObservationStatus.NOT_AVAILABLE);

        assertThat(entity.getNumericValue())
                .isNull();

        assertThat(entity.getTextValue())
                .isNull();

        assertThat(entity.getBooleanValue())
                .isNull();

        assertThat(entity.getDateValue())
                .isNull();
    }

    @Test
    void shouldNotPersistQuestionnaireVariableDataType() {

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.DECIMAL,
                        new BigDecimal("10.25"),
                        null,
                        null,
                        null,
                        "USD",
                        null);

        DataCollectionObservationEntity entity =
                mapper.toEntity(observation);

        assertThat(entity.getNumericValue())
                .isEqualByComparingTo("10.25");

        /*
         * The questionnaire variable data type is deliberately
         * not represented in the observation entity.
         */
        assertThat(
                DataCollectionObservationEntity.class
                        .getDeclaredFields())
                .noneMatch(field ->
                        field.getName().equals("dataType"));
    }
}
