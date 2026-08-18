package org.afdb.aikp.modules.campaign.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CampaignDescriptionTest {

    @Test
    void shouldTrimDescription() {
        CampaignDescription description =
                CampaignDescription.of("  AIKP data collection campaign.  ");

        assertEquals(
                "AIKP data collection campaign.",
                description.getValue()
        );
    }

    @Test
    void shouldRejectNullDescription() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignDescription.of(null)
        );
    }

    @Test
    void shouldRejectBlankDescription() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignDescription.of("   ")
        );
    }

    @Test
    void shouldRejectDescriptionLongerThan1000Characters() {
        String value = "A".repeat(1001);

        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignDescription.of(value)
        );
    }
}
