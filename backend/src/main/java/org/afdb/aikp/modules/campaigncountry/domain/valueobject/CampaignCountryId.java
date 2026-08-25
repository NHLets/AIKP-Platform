package org.afdb.aikp.modules.campaigncountry.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Unique identifier of a CampaignCountry association.
 */
public final class CampaignCountryId {

    private final UUID value;

    private CampaignCountryId(UUID value) {
        this.value = Objects.requireNonNull(
                value,
                "CampaignCountry ID cannot be null.");
    }

    public static CampaignCountryId of(UUID value) {
        return new CampaignCountryId(value);
    }

    public static CampaignCountryId generate() {
        return new CampaignCountryId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof CampaignCountryId other)) {
            return false;
        }

        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
