package org.afdb.aikp.modules.questionnaire.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

import java.util.regex.Pattern;

/**
 * Business version of a Questionnaire.
 *
 * <p>
 * Represents the functional version of a questionnaire
 * (e.g. 1, 1.0, 1.1, 2.0, 2026.1).
 * </p>
 *
 * <p>
 * This value object must not be confused with the JPA optimistic locking
 * version (@Version) defined in {@code AuditableEntity}.
 * </p>
 */
public final class QuestionnaireVersion extends ValueObject<String> {

    /**
     * Maximum length.
     */
    public static final int MAX_LENGTH = 20;

    /**
     * Accepted version pattern.
     *
     * Examples:
     * 1
     * 1.0
     * 1.1
     * 2.0
     * 2026.1
     */
    private static final Pattern VERSION_PATTERN =
            Pattern.compile("\\d+(\\.\\d+)*");

    private QuestionnaireVersion(String value) {
        super(normalize(value));
    }

    /**
     * Creates a QuestionnaireVersion.
     *
     * @param value business version
     * @return QuestionnaireVersion
     */
    public static QuestionnaireVersion of(String value) {
        return new QuestionnaireVersion(value);
    }

    /**
     * Returns the initial business version.
     *
     * @return QuestionnaireVersion 1.0
     */
    public static QuestionnaireVersion initial() {
        return new QuestionnaireVersion("1.0");
    }

    /**
     * Normalizes and validates the version.
     */
    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Questionnaire version cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Questionnaire version cannot exceed "
                            + MAX_LENGTH + " characters.");
        }

        if (!VERSION_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Questionnaire version must follow the format "
                            + "'1', '1.0', '1.1', '2.0', '2026.1', etc.");
        }

        return value;
    }
}