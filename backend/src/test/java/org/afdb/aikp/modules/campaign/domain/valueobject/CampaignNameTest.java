package org.afdb.aikp.modules.campaign.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CampaignNameTest {

    @Test
    void shouldTrimName() {
        CampaignName name = CampaignName.of("  AIKP Data Collection 2026  ");

        assertEquals("AIKP Data Collection 2026", name.getValue());
    }

    @Test
    void shouldRejectNullName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignName.of(null)
        );
    }

    @Test
    void shouldRejectBlankName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignName.of("   ")
        );
    }

    @Test
    void shouldRejectNameLongerThan255Characters() {
        String value = "A".repeat(256);

        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignName.of(value)
        );
    }
}
