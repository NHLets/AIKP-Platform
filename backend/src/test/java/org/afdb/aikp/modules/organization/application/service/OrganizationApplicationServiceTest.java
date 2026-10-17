package org.afdb.aikp.modules.organization.application.service;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.application.command.ActivateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.port.out.DataCollectionUsageChecker;
import org.afdb.aikp.modules.organization.application.command.CreateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.DeactivateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.DeleteOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.UpdateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.query.GetActiveOrganizationsQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationsByCountryQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationsByTypeQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationsQuery;
import org.afdb.aikp.modules.organization.application.response.OrganizationResponse;
import org.afdb.aikp.modules.organization.application.response.OrganizationSummary;
import org.afdb.aikp.modules.organization.domain.enums.OrganizationType;
import org.afdb.aikp.modules.organization.domain.exception.OrganizationNotFoundException;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.service.OrganizationDomainService;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationApplicationServiceTest {

    private OrganizationRepository repository;

    private OrganizationDomainService domainService;

    private DataCollectionUsageChecker dataCollectionUsageChecker;

    private OrganizationApplicationService service;

    @BeforeEach
    void setUp() {

        repository = mock(OrganizationRepository.class);
        domainService = mock(OrganizationDomainService.class);
        dataCollectionUsageChecker = mock(DataCollectionUsageChecker.class);

        service = new OrganizationApplicationService(
                repository,
                domainService,
                dataCollectionUsageChecker);
    }

    @Test
    void shouldGetOrganization() {

        Organization organization =
                createOrganization();

        when(repository.findById(
                organization.getOrganizationId()))
                .thenReturn(Optional.of(organization));

        OrganizationResponse response =
                service.get(
                        new GetOrganizationQuery(
                                organization.getOrganizationId()
                                        .getValue()));

        assertNotNull(response);

        assertEquals(
                organization.getOrganizationId().getValue(),
                response.id());

        assertEquals(
                "AFDB",
                response.code());

        assertEquals(
                "African Development Bank",
                response.name());

        assertEquals(
                "MINISTRY",
                response.type());

        assertEquals(
                countryId().getValue(),
                response.countryId());

        assertTrue(response.active());

        verify(repository).findById(
                organization.getOrganizationId());
    }

    @Test
    void shouldThrowWhenOrganizationDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(repository.findById(
                OrganizationId.of(id)))
                .thenReturn(Optional.empty());

        assertThrows(
                OrganizationNotFoundException.class,
                () -> service.get(
                        new GetOrganizationQuery(id)));

        verify(repository).findById(
                OrganizationId.of(id));
    }

    @Test
    void shouldGetAllOrganizations() {

        Organization afdb =
                createOrganization();

        Organization comesa =
                Organization.create(
                        OrganizationId.generate(),
                        OrganizationCode.of("COMESA"),
                        OrganizationName.of(
                                "Common Market for Eastern and Southern Africa"),
                        OrganizationType.MINISTRY,
                        countryId());

        when(repository.findAll())
                .thenReturn(List.of(
                        afdb,
                        comesa));

        List<OrganizationSummary> result =
                service.getAll(
                        new GetOrganizationsQuery());

        assertEquals(2, result.size());

        assertEquals(
                "AFDB",
                result.get(0).code());

        assertEquals(
                "COMESA",
                result.get(1).code());

        verify(repository).findAll();
    }

    @Test
    void shouldGetActiveOrganizations() {

        Organization organization =
                createOrganization();

        when(repository.findActive())
                .thenReturn(List.of(organization));

        List<OrganizationSummary> result =
                service.getActive(
                        new GetActiveOrganizationsQuery());

        assertEquals(1, result.size());

        assertEquals(
                "AFDB",
                result.get(0).code());

        assertTrue(
                result.get(0).active());

        verify(repository).findActive();
    }

    @Test
    void shouldGetOrganizationsByCountry() {

        Organization organization =
                createOrganization();

        when(repository.findByCountryId(
                countryId()))
                .thenReturn(List.of(organization));

        List<OrganizationSummary> result =
                service.getByCountry(
                        new GetOrganizationsByCountryQuery(
                                countryId().getValue()));

        assertEquals(1, result.size());

        assertEquals(
                "AFDB",
                result.get(0).code());

        assertEquals(
                countryId().getValue(),
                result.get(0).countryId());

        verify(repository).findByCountryId(
                countryId());
    }

    @Test
    void shouldGetOrganizationsByType() {

        Organization organization =
                createOrganization();

        when(repository.findByType(
                OrganizationType.MINISTRY))
                .thenReturn(List.of(organization));

        List<OrganizationSummary> result =
                service.getByType(
                        new GetOrganizationsByTypeQuery(
                                "MINISTRY"));

        assertEquals(1, result.size());

        assertEquals(
                "AFDB",
                result.get(0).code());

        assertEquals(
                "MINISTRY",
                result.get(0).type());

        verify(repository).findByType(
                OrganizationType.MINISTRY);
    }

    @Test
    void shouldCreateOrganization() {

        UUID id = UUID.randomUUID();

        Organization savedOrganization =
                Organization.create(
                        OrganizationId.of(id),
                        OrganizationCode.of("AFDB"),
                        OrganizationName.of(
                                "African Development Bank"),
                        OrganizationType.MINISTRY,
                        countryId());

        when(repository.save(
                any(Organization.class)))
                .thenReturn(savedOrganization);

        CreateOrganizationCommand command =
                new CreateOrganizationCommand(
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId().getValue());

        OrganizationResponse response =
                service.create(command);

        assertNotNull(response);

        assertEquals(
                id,
                response.id());

        assertEquals(
                "AFDB",
                response.code());

        assertEquals(
                "African Development Bank",
                response.name());

        assertEquals(
                "MINISTRY",
                response.type());

        assertEquals(
                countryId().getValue(),
                response.countryId());

        assertTrue(response.active());

        verify(domainService).validateCreation(
                OrganizationCode.of("AFDB"));

        verify(repository).save(
                any(Organization.class));
    }

    @Test
    void shouldUpdateOrganization() {

        OrganizationId id =
                OrganizationId.of(
                        UUID.randomUUID());

        Organization organization =
                Organization.create(
                        id,
                        OrganizationCode.of("AFDB"),
                        OrganizationName.of(
                                "African Development Bank"),
                        OrganizationType.MINISTRY,
                        countryId());

        when(repository.findById(id))
                .thenReturn(Optional.of(organization));

        when(repository.save(organization))
                .thenReturn(organization);

        UpdateOrganizationCommand command =
                new UpdateOrganizationCommand(
                        id.getValue(),
                        "COMESA",
                        "Common Market for Eastern and Southern Africa",
                        "REGULATOR",
                        countryId().getValue());

        OrganizationResponse response =
                service.update(command);

        assertNotNull(response);

        assertEquals(
                id.getValue(),
                response.id());

        assertEquals(
                "COMESA",
                response.code());

        assertEquals(
                "Common Market for Eastern and Southern Africa",
                response.name());

        assertEquals(
                "REGULATOR",
                response.type());

        assertEquals(
                countryId().getValue(),
                response.countryId());

        verify(domainService).validateUpdate(
                id,
                OrganizationCode.of("COMESA"));

        verify(repository).save(
                organization);
    }

    @Test
    void shouldActivateOrganization() {

        Organization organization =
                createOrganization();

        organization.deactivate();

        when(repository.findById(
                organization.getOrganizationId()))
                .thenReturn(Optional.of(organization));

        when(repository.save(organization))
                .thenReturn(organization);

        OrganizationResponse response =
                service.activate(
                        new ActivateOrganizationCommand(
                                organization
                                        .getOrganizationId()
                                        .getValue()));

        assertTrue(
                response.active());

        verify(repository).findById(
                organization.getOrganizationId());

        verify(repository).save(
                organization);
    }

    @Test
    void shouldDeactivateOrganization() {

        Organization organization =
                createOrganization();

        when(repository.findById(
                organization.getOrganizationId()))
                .thenReturn(Optional.of(organization));

        when(repository.save(organization))
                .thenReturn(organization);

        OrganizationResponse response =
                service.deactivate(
                        new DeactivateOrganizationCommand(
                                organization
                                        .getOrganizationId()
                                        .getValue()));

        assertFalse(
                response.active());

        verify(repository).findById(
                organization.getOrganizationId());

        verify(repository).save(
                organization);
    }

    @Test
    void shouldDeleteOrganizationWhenNotUsedByAnyDataCollection() {
        Organization organization = createOrganization();
        OrganizationId id = organization.getOrganizationId();

        when(repository.findById(id))
                .thenReturn(Optional.of(organization));

        when(dataCollectionUsageChecker
                .isUsedByAnyDataCollection(id.getValue()))
                .thenReturn(false);

        service.delete(
                new DeleteOrganizationCommand(id.getValue()));

        verify(repository).findById(id);
        verify(dataCollectionUsageChecker)
                .isUsedByAnyDataCollection(id.getValue());
        verify(repository).delete(organization);
    }

    @Test
    void shouldRejectDeleteWhenOrganizationIsResponsibleOrganization() {
        Organization organization = createOrganization();
        OrganizationId id = organization.getOrganizationId();

        when(repository.findById(id))
                .thenReturn(Optional.of(organization));

        when(dataCollectionUsageChecker
                .isUsedByAnyDataCollection(id.getValue()))
                .thenReturn(true);

        assertThrows(
                org.afdb.aikp.modules.organization.domain.exception
                        .OrganizationInUseException.class,
                () -> service.delete(
                        new DeleteOrganizationCommand(id.getValue())));

        verify(repository).findById(id);
        verify(dataCollectionUsageChecker)
                .isUsedByAnyDataCollection(id.getValue());
        verify(repository, never()).delete(any());
    }

    @Test
    void shouldRejectDeleteWhenOrganizationIsOperatorOrganization() {
        Organization organization = createOrganization();
        OrganizationId id = organization.getOrganizationId();

        when(repository.findById(id))
                .thenReturn(Optional.of(organization));

        when(dataCollectionUsageChecker
                .isUsedByAnyDataCollection(id.getValue()))
                .thenReturn(true);

        assertThrows(
                org.afdb.aikp.modules.organization.domain.exception
                        .OrganizationInUseException.class,
                () -> service.delete(
                        new DeleteOrganizationCommand(id.getValue())));

        verify(repository, never()).delete(any());
    }

    @Test
    void shouldRejectDeleteWhenOrganizationIsDataCollector() {
        Organization organization = createOrganization();
        OrganizationId id = organization.getOrganizationId();

        when(repository.findById(id))
                .thenReturn(Optional.of(organization));

        when(dataCollectionUsageChecker
                .isUsedByAnyDataCollection(id.getValue()))
                .thenReturn(true);

        assertThrows(
                org.afdb.aikp.modules.organization.domain.exception
                        .OrganizationInUseException.class,
                () -> service.delete(
                        new DeleteOrganizationCommand(id.getValue())));

        verify(repository, never()).delete(any());
    }

    @Test
    void shouldThrowWhenDeletingOrganizationDoesNotExist() {
        UUID id = UUID.randomUUID();
        OrganizationId organizationId = OrganizationId.of(id);

        when(repository.findById(organizationId))
                .thenReturn(Optional.empty());

        assertThrows(
                OrganizationNotFoundException.class,
                () -> service.delete(
                        new DeleteOrganizationCommand(id)));

        verify(repository).findById(organizationId);
        verifyNoInteractions(dataCollectionUsageChecker);
        verify(repository, never()).delete(any());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingOrganization() {

        UUID id = UUID.randomUUID();

        when(repository.findById(
                OrganizationId.of(id)))
                .thenReturn(Optional.empty());

        UpdateOrganizationCommand command =
                new UpdateOrganizationCommand(
                        id,
                        "AFDB",
                        "African Development Bank",
                        "MINISTRY",
                        countryId().getValue());

        assertThrows(
                OrganizationNotFoundException.class,
                () -> service.update(command));

        verify(repository).findById(
                OrganizationId.of(id));

        verify(repository, never())
                .save(any());
    }

    @Test
    void shouldThrowWhenDeletingNonExistingOrganization() {

        UUID id = UUID.randomUUID();

        when(repository.findById(
                OrganizationId.of(id)))
                .thenReturn(Optional.empty());

        assertThrows(
                OrganizationNotFoundException.class,
                () -> service.delete(
                        new DeleteOrganizationCommand(id)));

        verify(repository).findById(
                OrganizationId.of(id));

        verify(repository, never())
                .delete(any());
    }

    private Organization createOrganization() {

        return Organization.create(
                OrganizationId.generate(),
                OrganizationCode.of("AFDB"),
                OrganizationName.of(
                        "African Development Bank"),
                OrganizationType.MINISTRY,
                countryId());
    }

    private CountryId countryId() {

        return CountryId.of(
                UUID.fromString(
                        "11111111-1111-1111-1111-111111111111"));
    }
}