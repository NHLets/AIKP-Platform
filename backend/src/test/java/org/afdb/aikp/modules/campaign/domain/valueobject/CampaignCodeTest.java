package org.afdb.aikp.modules.campaign.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CampaignCodeTest {

    @Test
    void shouldNormalizeCode() {
        CampaignCode code = CampaignCode.of("  aikp-2026  ");

        assertEquals("AIKP-2026", code.getValue());
    }

    @Test
    void shouldRejectNullCode() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignCode.of(null)
        );
    }

    @Test
    void shouldRejectBlankCode() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignCode.of("   ")
        );
    }

    @Test
    void shouldRejectCodeLongerThan50Characters() {
        String value = "A".repeat(51);

        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignCode.of(value)
        );
    }

    @Test
    void shouldAcceptHyphenInCode() {
        CampaignCode code = CampaignCode.of("AIKP-2026");

        assertEquals("AIKP-2026", code.getValue());
    }

    @Test
    void shouldAcceptLettersDigitsUnderscoresAndHyphens() {
        CampaignCode code =
                CampaignCode.of("AIKP_POWER-2026_01");

        assertEquals(
                "AIKP_POWER-2026_01",
                code.getValue()
        );
    }

    @Test
    void shouldRejectInvalidCharacters() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignCode.of("AIKP 2026")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignCode.of("AIKP/2026")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignCode.of("AIKP@2026")
        );
    }

    @Test
    void shouldRejectCodeStartingWithHyphen() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignCode.of("-AIKP2026")
        );
    }

    @Test
    void shouldRejectCodeStartingWithUnderscore() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CampaignCode.of("_AIKP2026")
        );
    }
}