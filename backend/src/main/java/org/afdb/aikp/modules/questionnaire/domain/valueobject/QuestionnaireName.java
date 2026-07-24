package org.afdb.aikp.modules.questionnaire.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Human-readable name of a Questionnaire.
 */
public final class QuestionnaireName extends ValueObject<String> {

    private QuestionnaireName(String value) {
        super(normalize(value));
    }

    public static QuestionnaireName of(String value) {
        return new QuestionnaireName(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Questionnaire name cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > 150) {
            throw new IllegalArgumentException(
                    "Questionnaire name cannot exceed 150 characters.");
        }

        return value;
    }
}