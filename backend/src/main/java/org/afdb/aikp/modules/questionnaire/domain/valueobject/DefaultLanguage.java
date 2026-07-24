package org.afdb.aikp.modules.questionnaire.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Default language of a Questionnaire.
 */
public final class DefaultLanguage extends ValueObject<String> {

    private DefaultLanguage(String value) {
        super(normalize(value));
    }

    public static DefaultLanguage of(String value) {
        return new DefaultLanguage(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Default language cannot be null or blank.");
        }

        value = value.trim().toLowerCase();

        if (!value.matches("[a-z]{2}")) {
            throw new IllegalArgumentException(
                    "Default language must be a valid ISO 639-1 language code.");
        }

        return value;
    }
}