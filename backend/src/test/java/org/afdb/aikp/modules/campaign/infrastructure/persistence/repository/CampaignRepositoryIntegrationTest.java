package org.afdb.aikp.modules.campaign.infrastructure.persistence.repository;

import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class CampaignRepositoryIntegrationTest {

    @Autowired
    private CampaignRepository campaignRepository;

    private Campaign campaign;

    @BeforeEach
    void setUp() {
        campaign = Campaign.create(
                CampaignCode.of("IT_CAMPAIGN_001"),
                CampaignName.of("Integration Test Campaign"),
                CampaignDescription.of(
                        "Campaign used to validate repository persistence."),
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 10, 31));
    }

    @AfterEach
    void tearDown() {
        campaignRepository.findById(campaign.getId())
                .ifPresent(campaignRepository::delete);
    }

    @Test
    void shouldSaveAndFindCampaignById() {

        Campaign saved = campaignRepository.save(campaign);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(campaign.getId());

        var found = campaignRepository.findById(campaign.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId())
                .isEqualTo(campaign.getId());
        assertThat(found.get().getCode())
                .isEqualTo(campaign.getCode());
        assertThat(found.get().getName())
                .isEqualTo(campaign.getName());
        assertThat(found.get().getDescription())
                .isEqualTo(campaign.getDescription());
        assertThat(found.get().getStartDate())
                .isEqualTo(campaign.getStartDate());
        assertThat(found.get().getEndDate())
                .isEqualTo(campaign.getEndDate());
        assertThat(found.get().getStatus())
                .isEqualTo(campaign.getStatus());
    }

    @Test
    void shouldFindCampaignByCode() {

        campaignRepository.save(campaign);

        var found = campaignRepository.findByCode(
                campaign.getCode());

        assertThat(found).isPresent();
        assertThat(found.get().getId())
                .isEqualTo(campaign.getId());
        assertThat(found.get().getCode())
                .isEqualTo(campaign.getCode());
    }

    @Test
    void shouldCheckWhetherCampaignCodeExists() {

        assertThat(
                campaignRepository.existsByCode(
                        campaign.getCode()))
                .isFalse();

        campaignRepository.save(campaign);

        assertThat(
                campaignRepository.existsByCode(
                        campaign.getCode()))
                .isTrue();
    }

    @Test
    void shouldDeleteCampaign() {

        campaignRepository.save(campaign);

        assertThat(
                campaignRepository.findById(
                        campaign.getId()))
                .isPresent();

        campaignRepository.delete(campaign);

        assertThat(
                campaignRepository.findById(
                        campaign.getId()))
                .isEmpty();
    }
}
