package org.afdb.aikp.modules.campaign.domain.valueobject;

import org.afdb.aikp.shared.domain.Identifier;

import java.util.UUID;

/**
 * Strongly typed identifier for Campaign.
 */
public final class CampaignId extends Identifier<UUID> {

    private CampaignId(UUID value) {
        super(value);
    }

    /**
     * Creates a Campaign identifier from an existing UUID.
     */
    public static CampaignId of(UUID value) {
        return new CampaignId(value);
    }

    /**
     * Generates a new Campaign identifier.
     */
    public static CampaignId generate() {
        return new CampaignId(UUID.randomUUID());
    }
}
