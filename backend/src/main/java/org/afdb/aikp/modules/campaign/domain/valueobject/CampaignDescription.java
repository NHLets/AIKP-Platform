package org.afdb.aikp.modules.campaign.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Detailed description of a Campaign.
 */
public final class CampaignDescription extends ValueObject<String> {

    private CampaignDescription(String value) {
        super(normalize(value));
    }

    public static CampaignDescription of(String value) {
        return new CampaignDescription(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Campaign description cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > 1000) {
            throw new IllegalArgumentException(
                    "Campaign description cannot exceed 1000 characters.");
        }

        return value;
    }
}
