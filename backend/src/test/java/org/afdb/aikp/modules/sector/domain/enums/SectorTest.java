package org.afdb.aikp.modules.sector.domain.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SectorTest {

    @Test
    void shouldDefineAllSupportedSectors() {

        assertThat(Sector.values())
                .containsExactly(
                        Sector.POWER,
                        Sector.TRANSPORT,
                        Sector.ICT,
                        Sector.WATER_SUPPLY_AND_SANITATION);
    }

    @Test
    void shouldResolveSectorByName() {

        assertThat(
                Sector.valueOf("POWER"))
                .isEqualTo(Sector.POWER);

        assertThat(
                Sector.valueOf("TRANSPORT"))
                .isEqualTo(Sector.TRANSPORT);

        assertThat(
                Sector.valueOf("ICT"))
                .isEqualTo(Sector.ICT);

        assertThat(
                Sector.valueOf(
                        "WATER_SUPPLY_AND_SANITATION"))
                .isEqualTo(
                        Sector.WATER_SUPPLY_AND_SANITATION);
    }
}
