package org.afdb.aikp.modules.questionnaire.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Unique business code of a Questionnaire.
 */
public final class QuestionnaireCode extends ValueObject<String> {

    private QuestionnaireCode(String value) {
        super(normalize(value));
    }

    public static QuestionnaireCode of(String value) {
        return new QuestionnaireCode(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Questionnaire code cannot be null or blank.");
        }

        value = value.trim().toUpperCase();

        if (value.length() > 50) {
            throw new IllegalArgumentException(
                    "Questionnaire code cannot exceed 50 characters.");
        }

        if (!value.matches("[A-Z0-9_]+")) {
            throw new IllegalArgumentException(
                    "Questionnaire code may contain only uppercase letters, digits and underscores.");
        }

        return value;
    }
}