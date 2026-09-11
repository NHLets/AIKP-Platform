package org.afdb.aikp.modules.collection.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for DataCollectionObservation.
 */
class DataCollectionObservationTest {

    @Test
    void shouldCreateDecimalObservation() {

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.DECIMAL,
                        new BigDecimal("123.45"),
                        null,
                        null,
                        null,
                        "USD",
                        null);

        assertEquals(
                ObservationStatus.PROVIDED,
                observation.getStatus());

        assertEquals(
                new BigDecimal("123.45"),
                observation.getNumericValue());

        assertEquals(
                2025,
                observation.getReferenceYear());
    }

    @Test
    void shouldCreateIntegerObservation() {

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2015,
                        QuestionnaireVariableDataType.INTEGER,
                        new BigDecimal("42"),
                        null,
                        null,
                        null,
                        null,
                        null);

        assertEquals(
                new BigDecimal("42"),
                observation.getNumericValue());
    }

    @Test
    void shouldRejectFractionalValueForIntegerObservation() {

        assertThrows(
                IllegalArgumentException.class,
                () -> DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.INTEGER,
                        new BigDecimal("42.5"),
                        null,
                        null,
                        null,
                        null,
                        null));
    }

    @Test
    void shouldRejectValueForNotApplicableObservation() {

        assertThrows(
                IllegalArgumentException.class,
                () -> DataCollectionObservation.restore(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.DECIMAL,
                        ObservationStatus.NOT_APPLICABLE,
                        new BigDecimal("10.5"),
                        null,
                        null,
                        null,
                        null,
                        null));
    }

    @Test
    void shouldCreateTextObservation() {

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2020,
                        QuestionnaireVariableDataType.TEXT,
                        null,
                        "Economic policy information",
                        null,
                        null,
                        null,
                        null);

        assertEquals(
                "Economic policy information",
                observation.getTextValue());
    }

    @Test
    void shouldCreateBooleanObservation() {

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2021,
                        QuestionnaireVariableDataType.BOOLEAN,
                        null,
                        null,
                        true,
                        null,
                        null,
                        null);

        assertEquals(
                Boolean.TRUE,
                observation.getBooleanValue());
    }

    @Test
    void shouldCreateDateObservation() {

        LocalDate date = LocalDate.of(2021, 12, 31);

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2021,
                        QuestionnaireVariableDataType.DATE,
                        null,
                        null,
                        null,
                        date,
                        null,
                        null);

        assertEquals(
                date,
                observation.getDateValue());
    }

    @Test
    void shouldAcceptMinimumReferenceYear() {

        DataCollectionObservation observation =
                createNumericObservation(2015);

        assertEquals(
                2015,
                observation.getReferenceYear());
    }

    @Test
    void shouldAcceptMaximumReferenceYear() {

        DataCollectionObservation observation =
                createNumericObservation(2025);

        assertEquals(
                2025,
                observation.getReferenceYear());
    }

    @Test
    void shouldRejectReferenceYearBefore2015() {

        assertThrows(
                IllegalArgumentException.class,
                () -> createNumericObservation(2014));
    }

    @Test
    void shouldRejectReferenceYearAfter2025() {

        assertThrows(
                IllegalArgumentException.class,
                () -> createNumericObservation(2026));
    }

    @Test
    void shouldRejectProvidedObservationWithoutValue() {

        assertThrows(
                IllegalArgumentException.class,
                () -> DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.DECIMAL,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null));
    }

    @Test
    void shouldRejectMultipleValues() {

        assertThrows(
                IllegalArgumentException.class,
                () -> DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.DECIMAL,
                        new BigDecimal("10.5"),
                        "Invalid",
                        null,
                        null,
                        null,
                        null));
    }

    @Test
    void shouldRejectIncompatibleValueType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.TEXT,
                        new BigDecimal("10.5"),
                        null,
                        null,
                        null,
                        null,
                        null));
    }

    @Test
    void shouldCreateNotAvailableObservationWithoutValue() {

        DataCollectionObservation observation =
                DataCollectionObservation.notAvailable(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.DECIMAL,
                        "USD",
                        "Source did not provide the information.");

        assertEquals(
                ObservationStatus.NOT_AVAILABLE,
                observation.getStatus());

        assertEquals(
                null,
                observation.getNumericValue());
    }

    @Test
    void shouldCreateNotApplicableObservationWithoutValue() {

        DataCollectionObservation observation =
                DataCollectionObservation.notApplicable(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.BOOLEAN,
                        null,
                        "Variable does not apply.");

        assertEquals(
                ObservationStatus.NOT_APPLICABLE,
                observation.getStatus());

        assertEquals(
                null,
                observation.getBooleanValue());
    }

    @Test
    void shouldRejectValueForNotAvailableObservation() {

        assertThrows(
                IllegalArgumentException.class,
                () -> DataCollectionObservation.restore(
                        DataCollectionObservationId.generate(),
                        DataCollectionId.generate(),
                        QuestionnaireVariableId.generate(),
                        2025,
                        QuestionnaireVariableDataType.DECIMAL,
                        ObservationStatus.NOT_AVAILABLE,
                        new BigDecimal("10.5"),
                        null,
                        null,
                        null,
                        "USD",
                        null));
    }

    private DataCollectionObservation createNumericObservation(
            int year) {

        return DataCollectionObservation.provided(
                DataCollectionObservationId.generate(),
                DataCollectionId.generate(),
                QuestionnaireVariableId.generate(),
                year,
                QuestionnaireVariableDataType.NUMBER,
                new BigDecimal("100"),
                null,
                null,
                null,
                null,
                null);
    }
}
