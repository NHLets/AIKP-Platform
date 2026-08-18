package org.afdb.aikp.modules.iam.role.domain.service;

import org.afdb.aikp.modules.iam.role.domain.exception.RoleAlreadyExistsException;
import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.role.domain.repository.RoleRepository;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleDomainServiceTest {

    @Test
    void shouldValidateCreationWhenNameIsAvailable() {

        RoleRepository repository =
                mock(RoleRepository.class);

        when(repository.existsByName(
                RoleName.of("Administrator")))
                .thenReturn(false);

        RoleDomainService service =
                new RoleDomainService(repository);

        assertDoesNotThrow(() ->
                service.validateCreation(
                        RoleName.of("Administrator")));

        verify(repository).existsByName(
                RoleName.of("Administrator"));
    }

    @Test
    void shouldRejectCreationWhenNameAlreadyExists() {

        RoleRepository repository =
                mock(RoleRepository.class);

        when(repository.existsByName(
                RoleName.of("Administrator")))
                .thenReturn(true);

        RoleDomainService service =
                new RoleDomainService(repository);

        assertThrows(
                RoleAlreadyExistsException.class,
                () -> service.validateCreation(
                        RoleName.of("Administrator")));
    }

    @Test
    void shouldValidateRenameWhenNameIsAvailable() {

        RoleRepository repository =
                mock(RoleRepository.class);

        RoleDomainService service =
                new RoleDomainService(repository);

        Role role = Role.create(
                RoleName.of("Administrator"),
                RoleDescription.of(
                        "Platform administrator"),
                false);

        when(repository.findByName(
                RoleName.of("Reviewer")))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() ->
                service.validateRename(
                        role.getId(),
                        RoleName.of("Reviewer")));
    }

    @Test
    void shouldRejectRenameWhenAnotherRoleUsesName() {

        RoleRepository repository =
                mock(RoleRepository.class);

        RoleDomainService service =
                new RoleDomainService(repository);

        Role current = Role.create(
                RoleName.of("Administrator"),
                RoleDescription.of(
                        "Platform administrator"),
                false);

        Role existing = Role.create(
                RoleName.of("Reviewer"),
                RoleDescription.of(
                        "Platform reviewer"),
                false);

        when(repository.findByName(
                RoleName.of("Reviewer")))
                .thenReturn(Optional.of(existing));

        assertThrows(
                RoleAlreadyExistsException.class,
                () -> service.validateRename(
                        current.getId(),
                        RoleName.of("Reviewer")));
    }

    @Test
    void shouldAllowRenameToSameName() {

        RoleRepository repository =
                mock(RoleRepository.class);

        RoleDomainService service =
                new RoleDomainService(repository);

        Role role = Role.create(
                RoleName.of("Administrator"),
                RoleDescription.of(
                        "Platform administrator"),
                false);

        when(repository.findByName(
                RoleName.of("Administrator")))
                .thenReturn(Optional.of(role));

        assertDoesNotThrow(() ->
                service.validateRename(
                        role.getId(),
                        RoleName.of("Administrator")));
    }
}