package org.afdb.aikp.modules.campaign.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;
import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;
import org.afdb.aikp.modules.campaign.infrastructure.persistence.entity.CampaignEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CampaignPersistenceMapperTest {

    @Test
    void shouldMapDomainToEntity() {

        Campaign campaign = Campaign.create(
                CampaignCode.of("AIKP_2026"),
                CampaignName.of("AIKP Data Collection 2026"),
                CampaignDescription.of(
                        "AIKP infrastructure data collection campaign."),
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 10, 30));

        CampaignEntity entity =
                CampaignPersistenceMapper.toEntity(campaign);

        assertThat(entity.getId())
                .isEqualTo(campaign.getId().getValue());

        assertThat(entity.getCode())
                .isEqualTo("AIKP_2026");

        assertThat(entity.getName())
                .isEqualTo("AIKP Data Collection 2026");

        assertThat(entity.getDescription())
                .isEqualTo(
                        "AIKP infrastructure data collection campaign.");

        assertThat(entity.getStartDate())
                .isEqualTo(LocalDate.of(2026, 8, 1));

        assertThat(entity.getEndDate())
                .isEqualTo(LocalDate.of(2026, 10, 30));

        assertThat(entity.getStatus())
                .isEqualTo(CampaignStatus.DRAFT);
    }

    @Test
    void shouldRestoreDomainFromEntity() {

        java.util.UUID id = java.util.UUID.randomUUID();

        CampaignEntity entity = new CampaignEntity(id);
        entity.setCode("AIKP_2026");
        entity.setName("AIKP Data Collection 2026");
        entity.setDescription(
                "AIKP infrastructure data collection campaign.");
        entity.setStartDate(LocalDate.of(2026, 8, 1));
        entity.setEndDate(LocalDate.of(2026, 10, 30));
        entity.setStatus(CampaignStatus.PLANNED);

        Campaign campaign =
                CampaignPersistenceMapper.toDomain(entity);

        assertThat(campaign.getId().getValue())
                .isEqualTo(id);

        assertThat(campaign.getCode().getValue())
                .isEqualTo("AIKP_2026");

        assertThat(campaign.getName().getValue())
                .isEqualTo("AIKP Data Collection 2026");

        assertThat(campaign.getDescription().getValue())
                .isEqualTo(
                        "AIKP infrastructure data collection campaign.");

        assertThat(campaign.getStartDate())
                .isEqualTo(LocalDate.of(2026, 8, 1));

        assertThat(campaign.getEndDate())
                .isEqualTo(LocalDate.of(2026, 10, 30));

        assertThat(campaign.getStatus())
                .isEqualTo(CampaignStatus.PLANNED);
    }

    @Test
    void shouldRejectNullDomainCampaign() {

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> CampaignPersistenceMapper.toEntity(null));
    }

    @Test
    void shouldRejectNullEntity() {

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> CampaignPersistenceMapper.toDomain(null));
    }
}
