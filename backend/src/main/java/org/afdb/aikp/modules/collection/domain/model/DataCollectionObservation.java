package org.afdb.aikp.modules.collection.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;
import org.afdb.aikp.shared.domain.AggregateRoot;

/**
 * Represents one statistical observation collected for a
 * questionnaire variable during a DataCollection.
 *
 * <p>
 * The data type belongs to the QuestionnaireVariable and is
 * therefore not persisted as part of the observation itself.
 * It is supplied when creating or restoring the observation
 * so that the domain can validate the supplied value.
 * </p>
 */
public final class DataCollectionObservation
        extends AggregateRoot<DataCollectionObservationId> {

    private static final int MIN_REFERENCE_YEAR = 2015;
    private static final int MAX_REFERENCE_YEAR = 2025;

    private final DataCollectionId dataCollectionId;

    private final QuestionnaireVariableId questionnaireVariableId;

    private final int referenceYear;

    private final ObservationStatus status;

    private final BigDecimal numericValue;

    private final String textValue;

    private final Boolean booleanValue;

    private final LocalDate dateValue;

    private final String selectedUnit;

    private final String comment;

    private DataCollectionObservation(
            DataCollectionObservationId id,
            DataCollectionId dataCollectionId,
            QuestionnaireVariableId questionnaireVariableId,
            int referenceYear,
            QuestionnaireVariableDataType dataType,
            ObservationStatus status,
            BigDecimal numericValue,
            String textValue,
            Boolean booleanValue,
            LocalDate dateValue,
            String selectedUnit,
            String comment) {

        super(Objects.requireNonNull(
                id,
                "DataCollectionObservation ID cannot be null."));

        this.dataCollectionId = Objects.requireNonNull(
                dataCollectionId,
                "Data collection ID cannot be null.");

        this.questionnaireVariableId = Objects.requireNonNull(
                questionnaireVariableId,
                "Questionnaire variable ID cannot be null.");

        validateReferenceYear(referenceYear);
        this.referenceYear = referenceYear;

        this.status = Objects.requireNonNull(
                status,
                "Observation status cannot be null.");

        validateValue(
                dataType,
                status,
                numericValue,
                textValue,
                booleanValue,
                dateValue);

        this.numericValue = numericValue;
        this.textValue = normalizeText(textValue);
        this.booleanValue = booleanValue;
        this.dateValue = dateValue;
        this.selectedUnit = normalizeText(selectedUnit);
        this.comment = normalizeText(comment);
    }

    public static DataCollectionObservation provided(
            DataCollectionObservationId id,
            DataCollectionId dataCollectionId,
            QuestionnaireVariableId questionnaireVariableId,
            int referenceYear,
            QuestionnaireVariableDataType dataType,
            BigDecimal numericValue,
            String textValue,
            Boolean booleanValue,
            LocalDate dateValue,
            String selectedUnit,
            String comment) {

        return new DataCollectionObservation(
                id,
                dataCollectionId,
                questionnaireVariableId,
                referenceYear,
                dataType,
                ObservationStatus.PROVIDED,
                numericValue,
                textValue,
                booleanValue,
                dateValue,
                selectedUnit,
                comment);
    }

    public static DataCollectionObservation notAvailable(
            DataCollectionObservationId id,
            DataCollectionId dataCollectionId,
            QuestionnaireVariableId questionnaireVariableId,
            int referenceYear,
            QuestionnaireVariableDataType dataType,
            String selectedUnit,
            String comment) {

        return new DataCollectionObservation(
                id,
                dataCollectionId,
                questionnaireVariableId,
                referenceYear,
                dataType,
                ObservationStatus.NOT_AVAILABLE,
                null,
                null,
                null,
                null,
                selectedUnit,
                comment);
    }

    public static DataCollectionObservation notApplicable(
            DataCollectionObservationId id,
            DataCollectionId dataCollectionId,
            QuestionnaireVariableId questionnaireVariableId,
            int referenceYear,
            QuestionnaireVariableDataType dataType,
            String selectedUnit,
            String comment) {

        return new DataCollectionObservation(
                id,
                dataCollectionId,
                questionnaireVariableId,
                referenceYear,
                dataType,
                ObservationStatus.NOT_APPLICABLE,
                null,
                null,
                null,
                null,
                selectedUnit,
                comment);
    }

    public static DataCollectionObservation restore(
            DataCollectionObservationId id,
            DataCollectionId dataCollectionId,
            QuestionnaireVariableId questionnaireVariableId,
            int referenceYear,
            QuestionnaireVariableDataType dataType,
            ObservationStatus status,
            BigDecimal numericValue,
            String textValue,
            Boolean booleanValue,
            LocalDate dateValue,
            String selectedUnit,
            String comment) {

        return new DataCollectionObservation(
                id,
                dataCollectionId,
                questionnaireVariableId,
                referenceYear,
                dataType,
                status,
                numericValue,
                textValue,
                booleanValue,
                dateValue,
                selectedUnit,
                comment);
    }

    public DataCollectionId getDataCollectionId() {
        return dataCollectionId;
    }

    public QuestionnaireVariableId getQuestionnaireVariableId() {
        return questionnaireVariableId;
    }

    public int getReferenceYear() {
        return referenceYear;
    }

    public ObservationStatus getStatus() {
        return status;
    }

    public BigDecimal getNumericValue() {
        return numericValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public LocalDate getDateValue() {
        return dateValue;
    }

    public String getSelectedUnit() {
        return selectedUnit;
    }

    public String getComment() {
        return comment;
    }

    private static void validateReferenceYear(int year) {

        if (year < MIN_REFERENCE_YEAR
                || year > MAX_REFERENCE_YEAR) {

            throw new IllegalArgumentException(
                    "Reference year must be between "
                            + MIN_REFERENCE_YEAR
                            + " and "
                            + MAX_REFERENCE_YEAR
                            + ".");
        }
    }

    private static void validateValue(
            QuestionnaireVariableDataType dataType,
            ObservationStatus status,
            BigDecimal numericValue,
            String textValue,
            Boolean booleanValue,
            LocalDate dateValue) {

        Objects.requireNonNull(
                dataType,
                "Questionnaire variable data type cannot be null.");

        int populatedValues = 0;

        if (numericValue != null) {
            populatedValues++;
        }

        if (textValue != null && !textValue.isBlank()) {
            populatedValues++;
        }

        if (booleanValue != null) {
            populatedValues++;
        }

        if (dateValue != null) {
            populatedValues++;
        }

        if (status != ObservationStatus.PROVIDED) {

            if (populatedValues != 0) {
                throw new IllegalArgumentException(
                        "A non-PROVIDED observation cannot contain a value.");
            }

            return;
        }

        if (populatedValues != 1) {
            throw new IllegalArgumentException(
                    "A PROVIDED observation must contain exactly one value.");
        }

        boolean numericType =
                dataType == QuestionnaireVariableDataType.NUMBER
                        || dataType == QuestionnaireVariableDataType.INTEGER
                        || dataType == QuestionnaireVariableDataType.DECIMAL
                        || dataType == QuestionnaireVariableDataType.PERCENTAGE;

        if (numericType && numericValue == null) {
            throw new IllegalArgumentException(
                    "A numeric questionnaire variable requires a numeric value.");
        }

        if (dataType == QuestionnaireVariableDataType.INTEGER
                && numericValue != null
                && numericValue.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException(
                    "An INTEGER questionnaire variable requires an integer value.");
        }

        if (dataType == QuestionnaireVariableDataType.TEXT
                && (textValue == null || textValue.isBlank())) {

            throw new IllegalArgumentException(
                    "A TEXT questionnaire variable requires a text value.");
        }

        if (dataType == QuestionnaireVariableDataType.BOOLEAN
                && booleanValue == null) {

            throw new IllegalArgumentException(
                    "A BOOLEAN questionnaire variable requires a boolean value.");
        }

        if (dataType == QuestionnaireVariableDataType.DATE
                && dateValue == null) {

            throw new IllegalArgumentException(
                    "A DATE questionnaire variable requires a date value.");
        }

        if (numericType && numericValue != null
                && (textValue != null
                || booleanValue != null
                || dateValue != null)) {

            throw new IllegalArgumentException(
                    "A numeric questionnaire variable cannot contain another value type.");
        }

        if (dataType == QuestionnaireVariableDataType.TEXT
                && (numericValue != null
                || booleanValue != null
                || dateValue != null)) {

            throw new IllegalArgumentException(
                    "A TEXT questionnaire variable cannot contain another value type.");
        }

        if (dataType == QuestionnaireVariableDataType.BOOLEAN
                && (numericValue != null
                || textValue != null
                || dateValue != null)) {

            throw new IllegalArgumentException(
                    "A BOOLEAN questionnaire variable cannot contain another value type.");
        }

        if (dataType == QuestionnaireVariableDataType.DATE
                && (numericValue != null
                || textValue != null
                || booleanValue != null)) {

            throw new IllegalArgumentException(
                    "A DATE questionnaire variable cannot contain another value type.");
        }
    }

    private static String normalizeText(String value) {

        if (value == null) {
            return null;
        }

        value = value.trim();

        return value.isBlank() ? null : value;
    }
}
