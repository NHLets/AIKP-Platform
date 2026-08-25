package org.afdb.aikp.modules.validation.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Comments associated with a data collection validation decision.
 */
public final class ValidationComments
        extends ValueObject<String> {

    private static final int MAX_LENGTH = 4000;

    private ValidationComments(String value) {
        super(value);
    }

    /**
     * Creates validation comments.
     *
     * <p>
     * A null or blank value represents the absence of comments.
     * </p>
     */
    public static ValidationComments of(String value) {

        if (value == null) {
            return null;
        }

        String normalizedValue = value.trim();

        if (normalizedValue.isEmpty()) {
            return null;
        }

        if (normalizedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Validation comments cannot exceed "
                            + MAX_LENGTH
                            + " characters.");
        }

        return new ValidationComments(normalizedValue);
    }
}
