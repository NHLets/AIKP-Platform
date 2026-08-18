package org.afdb.aikp.modules.campaign.domain.valueobject;

import org.afdb.aikp.shared.domain.ValueObject;

/**
 * Name of a Campaign.
 */
public final class CampaignName extends ValueObject<String> {

    private CampaignName(String value) {
        super(normalize(value));
    }

    public static CampaignName of(String value) {
        return new CampaignName(value);
    }

    private static String normalize(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Campaign name cannot be null or blank.");
        }

        value = value.trim();

        if (value.length() > 255) {
            throw new IllegalArgumentException(
                    "Campaign name cannot exceed 255 characters.");
        }

        return value;
    }
}
