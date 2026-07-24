package org.afdb.aikp.modules.questionnaire.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Detailed description of a Questionnaire.
 */
public final class QuestionnaireDescription extends ValueObject<String> {

    private QuestionnaireDescription(String value) {
        super(normalize(value));
    }

    public static QuestionnaireDescription of(String value) {
        return new QuestionnaireDescription(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Questionnaire description cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > 1000) {
            throw new IllegalArgumentException(
                    "Questionnaire description cannot exceed 1000 characters.");
        }

        return value;
    }
}
