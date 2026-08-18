package org.afdb.aikp.modules.campaign.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CampaignIdTest {

    @Test
    void shouldGenerateCampaignId() {
        CampaignId id = CampaignId.generate();

        assertNotNull(id);
        assertNotNull(id.getValue());
    }

    @Test
    void shouldCreateCampaignIdFromUuid() {
        UUID uuid = UUID.randomUUID();

        CampaignId id = CampaignId.of(uuid);

        assertEquals(uuid, id.getValue());
    }

    @Test
    void shouldRejectNullUuid() {
        assertThrows(
                NullPointerException.class,
                () -> CampaignId.of(null)
        );
    }
}
