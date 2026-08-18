package org.afdb.aikp.modules.campaign.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Unique business code of a Campaign.
 */
public final class CampaignCode extends ValueObject<String> {

    private CampaignCode(String value) {
        super(normalize(value));
    }

    public static CampaignCode of(String value) {
        return new CampaignCode(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Campaign code cannot be null or blank.");
        }

        value = value.trim().toUpperCase();

        if (value.length() > 50) {
            throw new IllegalArgumentException(
                    "Campaign code cannot exceed 50 characters.");
        }

        if (!value.matches("[A-Z0-9_]+")) {
            throw new IllegalArgumentException(
                    "Campaign code may contain only uppercase letters, digits and underscores.");
        }

        return value;
    }
}
