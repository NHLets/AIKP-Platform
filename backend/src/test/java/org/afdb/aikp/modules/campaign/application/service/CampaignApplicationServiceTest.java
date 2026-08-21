package org.afdb.aikp.modules.campaign.application.service;

import org.afdb.aikp.modules.campaign.application.command.ActivateCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.ArchiveCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.CompleteCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.CreateCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.DeleteCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.PlanCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.UpdateCampaignCommand;
import org.afdb.aikp.modules.campaign.application.query.GetCampaignQuery;
import org.afdb.aikp.modules.campaign.application.response.CampaignResponse;
import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignCodeAlreadyExistsException;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignLifecycleException;
import org.afdb.aikp.modules.campaign.domain.exception.CampaignNotFoundException;
import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignApplicationServiceTest {

    @Mock
    private CampaignRepository repository;

    private CampaignApplicationService service;

    private Campaign campaign;

    @BeforeEach
    void setUp() {

        service = new CampaignApplicationService(repository);

        campaign = Campaign.create(
                CampaignCode.of("AIKP_2026"),
                org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName.of(
                        "AIKP Data Collection 2026"),
                org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription.of(
                        "AIKP infrastructure data collection campaign."),
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 10, 30));
    }

    @Test
    void shouldCreateCampaign() {

        CreateCampaignCommand command =
                new CreateCampaignCommand(
                        "AIKP_2026",
                        "AIKP Data Collection 2026",
                        "AIKP infrastructure data collection campaign.",
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 10, 30));

        when(repository.existsByCode(any(CampaignCode.class)))
                .thenReturn(false);

        when(repository.save(any(Campaign.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CampaignResponse response = service.create(command);

        assertThat(response.code())
                .isEqualTo("AIKP_2026");

        assertThat(response.status())
                .isEqualTo(CampaignStatus.DRAFT);

        assertThat(response.active())
                .isFalse();

        verify(repository).existsByCode(any(CampaignCode.class));
        verify(repository).save(any(Campaign.class));
    }

    @Test
    void shouldRejectDuplicateCampaignCode() {

        CreateCampaignCommand command =
                new CreateCampaignCommand(
                        "AIKP_2026",
                        "AIKP Data Collection 2026",
                        "AIKP infrastructure data collection campaign.",
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 10, 30));

        when(repository.existsByCode(any(CampaignCode.class)))
                .thenReturn(true);

        assertThatThrownBy(
                () -> service.create(command))
                .isInstanceOf(CampaignCodeAlreadyExistsException.class)
                .hasMessageContaining(
                        "Campaign code already exists");

        verify(repository, never())
                .save(any(Campaign.class));
    }

    @Test
    void shouldGetCampaignById() {

        when(repository.findById(any()))
                .thenReturn(Optional.of(campaign));

        CampaignResponse response =
                service.getById(
                        new GetCampaignQuery(
                                campaign.getId().getValue()));

        assertThat(response.id())
                .isEqualTo(campaign.getId().getValue());

        assertThat(response.code())
                .isEqualTo("AIKP_2026");
    }

    @Test
    void shouldRejectUnknownCampaignId() {

        UUID id = UUID.randomUUID();

        when(repository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.getById(
                        new GetCampaignQuery(id)))
                .isInstanceOf(CampaignNotFoundException.class)
                .hasMessageContaining(
                        "Campaign not found with id");
    }

    @Test
    void shouldGetCampaignByCode() {

        when(repository.findByCode(any()))
                .thenReturn(Optional.of(campaign));

        CampaignResponse response =
                service.getByCode("AIKP_2026");

        assertThat(response.id())
                .isEqualTo(campaign.getId().getValue());

        assertThat(response.code())
                .isEqualTo("AIKP_2026");
    }

    @Test
    void shouldRejectUnknownCampaignCode() {

        when(repository.findByCode(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.getByCode("UNKNOWN"))
                .isInstanceOf(CampaignNotFoundException.class)
                .hasMessageContaining(
                        "Campaign not found with code");
    }

    @Test
    void shouldUpdateCampaign() {

        when(repository.findById(any()))
                .thenReturn(Optional.of(campaign));

        when(repository.save(any(Campaign.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CampaignResponse response =
                service.update(
                        new UpdateCampaignCommand(
                                campaign.getId().getValue(),
                                "Updated Campaign",
                                "Updated description.",
                                LocalDate.of(2026, 8, 5),
                                LocalDate.of(2026, 11, 5)));

        assertThat(response.name())
                .isEqualTo("Updated Campaign");

        assertThat(response.description())
                .isEqualTo("Updated description.");

        assertThat(response.startDate())
                .isEqualTo(LocalDate.of(2026, 8, 5));

        assertThat(response.endDate())
                .isEqualTo(LocalDate.of(2026, 11, 5));

        verify(repository).save(campaign);
    }

    @Test
    void shouldPlanCampaign() {

        when(repository.findById(any()))
                .thenReturn(Optional.of(campaign));

        when(repository.save(any(Campaign.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CampaignResponse response =
                service.plan(
                        new PlanCampaignCommand(
                                campaign.getId().getValue()));

        assertThat(response.status())
                .isEqualTo(CampaignStatus.PLANNED);
    }

    @Test
    void shouldActivateCampaign() {

        campaign.plan();

        when(repository.findById(any()))
                .thenReturn(Optional.of(campaign));

        when(repository.save(any(Campaign.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CampaignResponse response =
                service.activate(
                        new ActivateCampaignCommand(
                                campaign.getId().getValue()));

        assertThat(response.status())
                .isEqualTo(CampaignStatus.ACTIVE);

        assertThat(response.active())
                .isTrue();
    }

    @Test
    void shouldCompleteCampaign() {

        campaign.plan();
        campaign.activate();

        when(repository.findById(any()))
                .thenReturn(Optional.of(campaign));

        when(repository.save(any(Campaign.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CampaignResponse response =
                service.complete(
                        new CompleteCampaignCommand(
                                campaign.getId().getValue()));

        assertThat(response.status())
                .isEqualTo(CampaignStatus.COMPLETED);
    }

    @Test
    void shouldArchiveCampaign() {

        campaign.plan();
        campaign.activate();
        campaign.complete();

        when(repository.findById(any()))
                .thenReturn(Optional.of(campaign));

        when(repository.save(any(Campaign.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CampaignResponse response =
                service.archive(
                        new ArchiveCampaignCommand(
                                campaign.getId().getValue()));

        assertThat(response.status())
                .isEqualTo(CampaignStatus.ARCHIVED);
    }

    @Test
    void shouldRejectInvalidLifecycleTransition() {

        when(repository.findById(any()))
                .thenReturn(Optional.of(campaign));

        assertThatThrownBy(
                () -> service.activate(
                        new ActivateCampaignCommand(
                                campaign.getId().getValue())))
                .isInstanceOf(CampaignLifecycleException.class);

        verify(repository, never())
                .save(any(Campaign.class));
    }

    @Test
    void shouldDeleteCampaign() {

        when(repository.findById(any()))
                .thenReturn(Optional.of(campaign));

        service.delete(
                new DeleteCampaignCommand(
                        campaign.getId().getValue()));

        verify(repository).delete(campaign);
    }
}
