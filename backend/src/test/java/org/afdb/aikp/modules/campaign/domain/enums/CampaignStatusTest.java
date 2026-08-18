package org.afdb.aikp.modules.campaign.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CampaignStatusTest {

    @Test
    void shouldContainExpectedLifecycleStatuses() {

        assertArrayEquals(
                new CampaignStatus[]{
                        CampaignStatus.DRAFT,
                        CampaignStatus.PLANNED,
                        CampaignStatus.ACTIVE,
                        CampaignStatus.COMPLETED,
                        CampaignStatus.ARCHIVED
                },
                CampaignStatus.values()
        );
    }

    @Test
    void shouldHaveFiveLifecycleStatuses() {
        assertEquals(5, CampaignStatus.values().length);
    }
}
