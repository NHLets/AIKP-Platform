package org.afdb.aikp.modules.organization.domain.service;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.exception.OrganizationCodeAlreadyExistsException;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;
import org.afdb.aikp.modules.organization.domain.enums.OrganizationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultOrganizationDomainServiceTest {

    @Mock
    private OrganizationRepository repository;

    private DefaultOrganizationDomainService service;

    @BeforeEach
    void setUp() {
        service = new DefaultOrganizationDomainService(repository);
    }

    @Test
    void shouldAllowCreationWhenCodeDoesNotExist() {

        OrganizationCode code =
                OrganizationCode.of("AFDB");

        when(repository.existsByCode(code))
                .thenReturn(false);

        service.validateCreation(code);

        verify(repository).existsByCode(code);
    }

    @Test
    void shouldRejectCreationWhenCodeAlreadyExists() {

        OrganizationCode code =
                OrganizationCode.of("AFDB");

        when(repository.existsByCode(code))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.validateCreation(code))
                .isInstanceOf(
                        OrganizationCodeAlreadyExistsException.class)
                .hasMessage("Organization code already exists: AFDB");

        verify(repository).existsByCode(code);
    }

    @Test
    void shouldRejectCreationWhenCodeIsNull() {

        assertThatThrownBy(() ->
                service.validateCreation(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(repository);
    }

    @Test
    void shouldAllowUpdateWhenCodeDoesNotExist() {

        OrganizationId organizationId =
                OrganizationId.generate();

        OrganizationCode code =
                OrganizationCode.of("AFDB");

        when(repository.findByCode(code))
                .thenReturn(Optional.empty());

        service.validateUpdate(
                organizationId,
                code);

        verify(repository).findByCode(code);
    }

    @Test
    void shouldAllowUpdateWhenCodeBelongsToSameOrganization() {

        OrganizationId organizationId =
                OrganizationId.generate();

        OrganizationCode code =
                OrganizationCode.of("AFDB");

        Organization organization = Organization.restore(
                organizationId,
                code,
                OrganizationName.of("African Development Bank"),
                OrganizationType.GOVERNMENT_AGENCY,
                CountryId.generate(),
                true
        );

        when(repository.findByCode(code))
                .thenReturn(Optional.of(organization));

        service.validateUpdate(
                organizationId,
                code);

        verify(repository).findByCode(code);
    }

    @Test
    void shouldRejectUpdateWhenCodeBelongsToAnotherOrganization() {

        OrganizationId organizationId =
                OrganizationId.generate();

        OrganizationId otherOrganizationId =
                OrganizationId.generate();

        OrganizationCode code =
                OrganizationCode.of("AFDB");

        Organization organization = Organization.restore(
                otherOrganizationId,
                code,
                OrganizationName.of("African Development Bank"),
                OrganizationType.GOVERNMENT_AGENCY,
                CountryId.generate(),
                true
        );

        when(repository.findByCode(code))
                .thenReturn(Optional.of(organization));

        assertThatThrownBy(() ->
                service.validateUpdate(
                        organizationId,
                        code))
                .isInstanceOf(
                        OrganizationCodeAlreadyExistsException.class)
                .hasMessage("Organization code already exists: AFDB");

        verify(repository).findByCode(code);
    }

    @Test
    void shouldRejectUpdateWhenOrganizationIdIsNull() {

        OrganizationCode code =
                OrganizationCode.of("AFDB");

        assertThatThrownBy(() ->
                service.validateUpdate(null, code))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(repository);
    }

    @Test
    void shouldRejectUpdateWhenCodeIsNull() {

        OrganizationId organizationId =
                OrganizationId.generate();

        assertThatThrownBy(() ->
                service.validateUpdate(
                        organizationId,
                        null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(repository);
    }
}
