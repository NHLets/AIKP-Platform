package org.afdb.aikp.modules.questionnaire.domain.model;

import java.util.Objects;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.shared.domain.AggregateRoot;

/**
 * Represents a structural group within a Questionnaire.
 *
 * <p>
 * Groups can form a hierarchy using a parent group. This allows the
 * same structure to support questionnaires with different organizational
 * levels, such as Dimension -> Policy Group.
 * </p>
 */
public class QuestionnaireGroup
        extends AggregateRoot<QuestionnaireGroupId> {

    private final QuestionnaireId questionnaireId;

    private final QuestionnaireGroupId parentGroupId;

    private final String code;

    private String name;

    private String description;

    private QuestionnaireGroupType groupType;

    private int displayOrder;

    private boolean active;

    private QuestionnaireGroup(
            QuestionnaireGroupId id,
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId parentGroupId,
            String code,
            String name,
            String description,
            QuestionnaireGroupType groupType,
            int displayOrder,
            boolean active) {

        super(Objects.requireNonNull(
                id,
                "Questionnaire group id cannot be null."));

        this.questionnaireId = Objects.requireNonNull(
                questionnaireId,
                "Questionnaire id cannot be null.");

        this.parentGroupId = parentGroupId;

        this.code = normalizeCode(code);

        this.name = normalizeName(name);

        this.description = normalizeDescription(description);

        this.groupType = Objects.requireNonNull(
                groupType,
                "Questionnaire group type cannot be null.");

        validateDisplayOrder(displayOrder);
        this.displayOrder = displayOrder;

        this.active = active;
    }

    /**
     * Creates a new QuestionnaireGroup.
     */
    public static QuestionnaireGroup create(
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId parentGroupId,
            String code,
            String name,
            String description,
            QuestionnaireGroupType groupType,
            int displayOrder) {

        return new QuestionnaireGroup(
                QuestionnaireGroupId.generate(),
                questionnaireId,
                parentGroupId,
                code,
                name,
                description,
                groupType,
                displayOrder,
                true);
    }

    /**
     * Restores an existing QuestionnaireGroup from persistence.
     */
    public static QuestionnaireGroup restore(
            QuestionnaireGroupId id,
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId parentGroupId,
            String code,
            String name,
            String description,
            QuestionnaireGroupType groupType,
            int displayOrder,
            boolean active) {

        return new QuestionnaireGroup(
                id,
                questionnaireId,
                parentGroupId,
                code,
                name,
                description,
                groupType,
                displayOrder,
                active);
    }

    // ---------------------------------------------------------------------
    // Business Behaviour
    // ---------------------------------------------------------------------

    public void rename(String newName) {
        this.name = normalizeName(newName);
    }

    public void changeDescription(String newDescription) {
        this.description = normalizeDescription(newDescription);
    }

    public void changeGroupType(
            QuestionnaireGroupType newGroupType) {

        this.groupType = Objects.requireNonNull(
                newGroupType,
                "Questionnaire group type cannot be null.");
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

    public QuestionnaireGroupId getParentGroupId() {
        return parentGroupId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public QuestionnaireGroupType getGroupType() {
        return groupType;
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

    private static String normalizeCode(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Questionnaire group code cannot be null or blank.");
        }

        value = value.trim().toUpperCase();

        if (value.length() > 100) {
            throw new IllegalArgumentException(
                    "Questionnaire group code cannot exceed 100 characters.");
        }

        return value;
    }

    private static String normalizeName(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Questionnaire group name cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > 200) {
            throw new IllegalArgumentException(
                    "Questionnaire group name cannot exceed 200 characters.");
        }

        return value;
    }

    private static String normalizeDescription(String value) {

        if (value == null) {
            return null;
        }

        value = value.trim();

        return value.isBlank() ? null : value;
    }

    private static void validateDisplayOrder(int value) {

        if (value < 0) {
            throw new IllegalArgumentException(
                    "Display order cannot be negative.");
        }
    }
}
