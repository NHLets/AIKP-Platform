package org.afdb.aikp.modules.campaign.application.mapper;

import org.afdb.aikp.modules.campaign.application.response.CampaignResponse;
import org.afdb.aikp.modules.campaign.application.response.CampaignSummary;
import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;
import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CampaignApplicationMapperTest {

    @Test
    void shouldMapCampaignToResponse() {

        Campaign campaign = Campaign.create(
                CampaignCode.of("AIKP_2026"),
                CampaignName.of("AIKP Data Collection 2026"),
                CampaignDescription.of(
                        "AIKP infrastructure data collection campaign."),
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 10, 30));

        CampaignResponse response =
                CampaignApplicationMapper.toResponse(campaign);

        assertThat(response.id())
                .isEqualTo(campaign.getId().getValue());
        assertThat(response.code())
                .isEqualTo("AIKP_2026");
        assertThat(response.name())
                .isEqualTo("AIKP Data Collection 2026");
        assertThat(response.description())
                .isEqualTo(
                        "AIKP infrastructure data collection campaign.");
        assertThat(response.startDate())
                .isEqualTo(LocalDate.of(2026, 8, 1));
        assertThat(response.endDate())
                .isEqualTo(LocalDate.of(2026, 10, 30));
        assertThat(response.status())
                .isEqualTo(CampaignStatus.DRAFT);
        assertThat(response.active())
                .isFalse();
    }

    @Test
    void shouldMapCampaignToSummary() {

        Campaign campaign = Campaign.create(
                CampaignCode.of("AIKP_2026"),
                CampaignName.of("AIKP Data Collection 2026"),
                CampaignDescription.of(
                        "AIKP infrastructure data collection campaign."),
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 10, 30));

        CampaignSummary summary =
                CampaignApplicationMapper.toSummary(campaign);

        assertThat(summary.id())
                .isEqualTo(campaign.getId().getValue());
        assertThat(summary.code())
                .isEqualTo("AIKP_2026");
        assertThat(summary.name())
                .isEqualTo("AIKP Data Collection 2026");
        assertThat(summary.startDate())
                .isEqualTo(LocalDate.of(2026, 8, 1));
        assertThat(summary.endDate())
                .isEqualTo(LocalDate.of(2026, 10, 30));
        assertThat(summary.status())
                .isEqualTo(CampaignStatus.DRAFT);
        assertThat(summary.active())
                .isFalse();
    }

    @Test
    void shouldReportActiveCampaignAsActive() {

        Campaign campaign = Campaign.create(
                CampaignCode.of("AIKP_2026"),
                CampaignName.of("AIKP Data Collection 2026"),
                CampaignDescription.of(
                        "AIKP infrastructure data collection campaign."),
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 10, 30));

        campaign.plan();
        campaign.activate();

        CampaignResponse response =
                CampaignApplicationMapper.toResponse(campaign);

        assertThat(response.status())
                .isEqualTo(CampaignStatus.ACTIVE);
        assertThat(response.active())
                .isTrue();
    }

    @Test
    void shouldRejectNullCampaignForResponse() {

        assertThatThrownBy(
                () -> CampaignApplicationMapper.toResponse(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Campaign cannot be null.");
    }

    @Test
    void shouldRejectNullCampaignForSummary() {

        assertThatThrownBy(
                () -> CampaignApplicationMapper.toSummary(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Campaign cannot be null.");
    }
}
