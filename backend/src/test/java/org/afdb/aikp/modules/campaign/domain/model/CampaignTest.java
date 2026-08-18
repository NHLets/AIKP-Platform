package org.afdb.aikp.modules.campaign.domain.model;

import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignLifecycleException;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CampaignTest {

    private Campaign createCampaign() {

        return Campaign.create(
                CampaignCode.of("AIKP_2026"),
                CampaignName.of("AIKP Data Collection 2026"),
                CampaignDescription.of(
                        "AIKP data collection campaign for 2026."),
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 10, 30));
    }

    @Test
    void shouldCreateCampaignInDraftStatus() {

        Campaign campaign = createCampaign();

        assertThat(campaign.getStatus())
                .isEqualTo(CampaignStatus.DRAFT);

        assertThat(campaign.isActive())
                .isTrue();
    }

    @Test
    void shouldCreateCampaignWithExpectedIdentityAndPeriod() {

        Campaign campaign = createCampaign();

        assertThat(campaign.getId())
                .isNotNull();

        assertThat(campaign.getCode().getValue())
                .isEqualTo("AIKP_2026");

        assertThat(campaign.getName().getValue())
                .isEqualTo("AIKP Data Collection 2026");

        assertThat(campaign.getDescription().getValue())
                .isEqualTo("AIKP data collection campaign for 2026.");

        assertThat(campaign.getStartDate())
                .isEqualTo(LocalDate.of(2026, 8, 1));

        assertThat(campaign.getEndDate())
                .isEqualTo(LocalDate.of(2026, 10, 30));
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() {

        assertThatThrownBy(() ->
                Campaign.create(
                        CampaignCode.of("AIKP_2026"),
                        CampaignName.of("AIKP Data Collection 2026"),
                        CampaignDescription.of(
                                "AIKP data collection campaign."),
                        LocalDate.of(2026, 10, 30),
                        LocalDate.of(2026, 8, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Campaign end date cannot be before start date.");
    }

    @Test
    void shouldPlanDraftCampaign() {

        Campaign campaign = createCampaign();

        campaign.plan();

        assertThat(campaign.getStatus())
                .isEqualTo(CampaignStatus.PLANNED);
    }

    @Test
    void shouldActivatePlannedCampaign() {

        Campaign campaign = createCampaign();

        campaign.plan();
        campaign.activate();

        assertThat(campaign.getStatus())
                .isEqualTo(CampaignStatus.ACTIVE);

        assertThat(campaign.isActive())
                .isTrue();
    }

    @Test
    void shouldCompleteActiveCampaign() {

        Campaign campaign = createCampaign();

        campaign.plan();
        campaign.activate();
        campaign.complete();

        assertThat(campaign.getStatus())
                .isEqualTo(CampaignStatus.COMPLETED);

        assertThat(campaign.isActive())
                .isFalse();
    }

    @Test
    void shouldArchiveCompletedCampaign() {

        Campaign campaign = createCampaign();

        campaign.plan();
        campaign.activate();
        campaign.complete();
        campaign.archive();

        assertThat(campaign.getStatus())
                .isEqualTo(CampaignStatus.ARCHIVED);

        assertThat(campaign.isActive())
                .isFalse();
    }

    @Test
    void shouldRejectPlanningWhenCampaignIsNotDraft() {

        Campaign campaign = createCampaign();

        campaign.plan();

        assertThatThrownBy(campaign::plan)
                .isInstanceOf(CampaignLifecycleException.class)
                .hasMessage(
                        "Only draft campaigns can be planned.");
    }

    @Test
    void shouldRejectActivationWhenCampaignIsNotPlanned() {

        Campaign campaign = createCampaign();

        assertThatThrownBy(campaign::activate)
                .isInstanceOf(CampaignLifecycleException.class)
                .hasMessage(
                        "Only planned campaigns can be activated.");
    }

    @Test
    void shouldRejectCompletionWhenCampaignIsNotActive() {

        Campaign campaign = createCampaign();

        assertThatThrownBy(campaign::complete)
                .isInstanceOf(CampaignLifecycleException.class)
                .hasMessage(
                        "Only active campaigns can be completed.");
    }

    @Test
    void shouldRejectArchivingWhenCampaignIsNotCompleted() {

        Campaign campaign = createCampaign();

        assertThatThrownBy(campaign::archive)
                .isInstanceOf(CampaignLifecycleException.class)
                .hasMessage(
                        "Only completed campaigns can be archived.");
    }

    @Test
    void shouldRenameCampaign() {

        Campaign campaign = createCampaign();

        campaign.rename(
                CampaignName.of("Updated AIKP Campaign"));

        assertThat(campaign.getName().getValue())
                .isEqualTo("Updated AIKP Campaign");
    }

    @Test
    void shouldChangeCampaignDescription() {

        Campaign campaign = createCampaign();

        campaign.changeDescription(
                CampaignDescription.of(
                        "Updated campaign description."));

        assertThat(campaign.getDescription().getValue())
                .isEqualTo("Updated campaign description.");
    }

    @Test
    void shouldChangeCampaignPeriod() {

        Campaign campaign = createCampaign();

        campaign.changePeriod(
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 11, 30));

        assertThat(campaign.getStartDate())
                .isEqualTo(LocalDate.of(2026, 9, 1));

        assertThat(campaign.getEndDate())
                .isEqualTo(LocalDate.of(2026, 11, 30));
    }

    @Test
    void shouldRejectInvalidPeriodWhenChangingCampaignPeriod() {

        Campaign campaign = createCampaign();

        assertThatThrownBy(() ->
                campaign.changePeriod(
                        LocalDate.of(2026, 11, 30),
                        LocalDate.of(2026, 9, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Campaign end date cannot be before start date.");
    }

    @Test
    void shouldDeactivateCampaign() {

        Campaign campaign = createCampaign();

        campaign.deactivate();

        assertThat(campaign.isActive())
                .isFalse();
    }

    @Test
    void shouldActivateCampaignAfterDeactivation() {

        Campaign campaign = createCampaign();

        campaign.deactivate();
        campaign.plan();
        campaign.activate();

        assertThat(campaign.getStatus())
                .isEqualTo(CampaignStatus.ACTIVE);

        assertThat(campaign.isActive())
                .isTrue();
    }
}
