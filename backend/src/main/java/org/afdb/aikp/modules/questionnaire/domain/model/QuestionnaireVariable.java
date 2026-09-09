package org.afdb.aikp.modules.questionnaire.domain.model;

import java.util.Objects;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;
import org.afdb.aikp.shared.domain.AggregateRoot;

/**
 * Represents a statistical variable collected through a questionnaire.
 *
 * <p>
 * A variable belongs to a questionnaire and may optionally belong to a
 * structural group, such as a Policy Group.
 * </p>
 */
public class QuestionnaireVariable
        extends AggregateRoot<QuestionnaireVariableId> {

    private final QuestionnaireId questionnaireId;

    private QuestionnaireGroupId questionnaireGroupId;

    private final String seriesCode;

    private String name;

    private String definition;

    private QuestionnaireVariableDataType dataType;

    private String unit;

    private boolean required;

    private int displayOrder;

    private boolean active;

    private QuestionnaireVariable(
            QuestionnaireVariableId id,
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId questionnaireGroupId,
            String seriesCode,
            String name,
            String definition,
            QuestionnaireVariableDataType dataType,
            String unit,
            boolean required,
            int displayOrder,
            boolean active) {

        super(Objects.requireNonNull(
                id,
                "Questionnaire variable id cannot be null."));

        this.questionnaireId = Objects.requireNonNull(
                questionnaireId,
                "Questionnaire id cannot be null.");

        this.questionnaireGroupId = questionnaireGroupId;

        this.seriesCode = normalizeSeriesCode(seriesCode);

        this.name = normalizeName(name);

        this.definition = normalizeText(definition);

        this.dataType = Objects.requireNonNull(
                dataType,
                "Questionnaire variable data type cannot be null.");

        this.unit = normalizeUnit(unit);

        this.required = required;

        validateDisplayOrder(displayOrder);
        this.displayOrder = displayOrder;

        this.active = active;
    }

    /**
     * Creates a new QuestionnaireVariable.
     */
    public static QuestionnaireVariable create(
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId questionnaireGroupId,
            String seriesCode,
            String name,
            String definition,
            QuestionnaireVariableDataType dataType,
            String unit,
            boolean required,
            int displayOrder) {

        return new QuestionnaireVariable(
                QuestionnaireVariableId.generate(),
                questionnaireId,
                questionnaireGroupId,
                seriesCode,
                name,
                definition,
                dataType,
                unit,
                required,
                displayOrder,
                true);
    }

    /**
     * Restores an existing QuestionnaireVariable from persistence.
     */
    public static QuestionnaireVariable restore(
            QuestionnaireVariableId id,
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId questionnaireGroupId,
            String seriesCode,
            String name,
            String definition,
            QuestionnaireVariableDataType dataType,
            String unit,
            boolean required,
            int displayOrder,
            boolean active) {

        return new QuestionnaireVariable(
                id,
                questionnaireId,
                questionnaireGroupId,
                seriesCode,
                name,
                definition,
                dataType,
                unit,
                required,
                displayOrder,
                active);
    }

    // ---------------------------------------------------------------------
    // Business behaviour
    // ---------------------------------------------------------------------

    public void assignToGroup(
            QuestionnaireGroupId newGroupId) {

        this.questionnaireGroupId = newGroupId;
    }

    public void removeFromGroup() {
        this.questionnaireGroupId = null;
    }

    public void rename(String newName) {
        this.name = normalizeName(newName);
    }

    public void changeDefinition(String newDefinition) {
        this.definition = normalizeText(newDefinition);
    }

    public void changeDataType(
            QuestionnaireVariableDataType newDataType) {

        this.dataType = Objects.requireNonNull(
                newDataType,
                "Questionnaire variable data type cannot be null.");
    }

    public void changeUnit(String newUnit) {
        this.unit = normalizeUnit(newUnit);
    }

    public void markAsRequired() {
        this.required = true;
    }

    public void markAsOptional() {
        this.required = false;
    }

    public void changeDisplayOrder(int newDisplayOrder) {
        validateDisplayOrder(newDisplayOrder);
        this.displayOrder = newDisplayOrder;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    // ---------------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------------

    public QuestionnaireId getQuestionnaireId() {
        return questionnaireId;
    }

    public QuestionnaireGroupId getQuestionnaireGroupId() {
        return questionnaireGroupId;
    }

    public String getSeriesCode() {
        return seriesCode;
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }

    public QuestionnaireVariableDataType getDataType() {
        return dataType;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isRequired() {
        return required;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public boolean isActive() {
        return active;
    }

    // ---------------------------------------------------------------------
    // Validation
    // ---------------------------------------------------------------------

    private static String normalizeSeriesCode(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Series code cannot be null or blank.");
        }

        value = value.trim().toUpperCase();

        if (value.length() > 100) {
            throw new IllegalArgumentException(
                    "Series code cannot exceed 100 characters.");
        }

        return value;
    }

    private static String normalizeName(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Questionnaire variable name cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > 500) {
            throw new IllegalArgumentException(
                    "Questionnaire variable name cannot exceed 500 characters.");
        }

        return value;
    }

    private static String normalizeText(String value) {

        if (value == null) {
            return null;
        }

        value = value.trim();

        return value.isBlank() ? null : value;
    }

    private static String normalizeUnit(String value) {

        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.isBlank()) {
            return null;
        }

        if (value.length() > 100) {
            throw new IllegalArgumentException(
                    "Unit cannot exceed 100 characters.");
        }

        return value;
    }

    private static void validateDisplayOrder(int value) {

        if (value < 0) {
            throw new IllegalArgumentException(
                    "Display order cannot be negative.");
        }
    }
}
