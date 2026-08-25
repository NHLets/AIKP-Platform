package org.afdb.aikp.modules.iam.role.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.iam.role.application.command.ActivateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.CreateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.DeactivateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.DeleteRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.UpdateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.query.GetActiveRolesQuery;
import org.afdb.aikp.modules.iam.role.application.query.GetRoleQuery;
import org.afdb.aikp.modules.iam.role.application.query.GetRolesQuery;
import org.afdb.aikp.modules.iam.role.application.response.RoleResponse;
import org.afdb.aikp.modules.iam.role.application.response.RoleSummary;
import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.domain.exception.RoleNotFoundException;
import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.role.domain.repository.RoleRepository;
import org.afdb.aikp.modules.iam.role.domain.service.RoleDomainService;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleApplicationServiceTest {

    private RoleRepository roleRepository;

    private RoleDomainService roleDomainService;

    private RoleApplicationService roleApplicationService;

    @BeforeEach
    void setUp() {

        roleRepository =
                mock(RoleRepository.class);

        roleDomainService =
                mock(RoleDomainService.class);

        roleApplicationService =
                new RoleApplicationService(
                        roleRepository,
                        roleDomainService);
    }

    @Test
    void shouldCreateRole() {

        RoleName name =
                RoleName.of("ADMIN");

        RoleDescription description =
                RoleDescription.of(
                        "Administrator role");

        CreateRoleCommand command =
                new CreateRoleCommand(
                        name,
                        description,
                        true);

        when(roleRepository.save(any(Role.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        RoleResponse response =
                roleApplicationService.create(command);

        assertThat(response)
                .isNotNull();

        assertThat(response.name())
                .isEqualTo("ADMIN");

        assertThat(response.description())
                .isEqualTo(
                        "Administrator role");

        assertThat(response.status())
                .isEqualTo(
                        RoleStatus.ACTIVE);

        assertThat(response.system())
                .isTrue();

        verify(roleDomainService)
                .validateCreation(name);

        verify(roleRepository)
                .save(any(Role.class));
    }

    @Test
    void shouldGetRoleById() {

        Role role =
                createRole(
                        "ADMIN",
                        "Administrator role",
                        true);

        when(roleRepository.findById(
                role.getId()))
                .thenReturn(
                        Optional.of(role));

        RoleResponse response =
                roleApplicationService.get(
                        new GetRoleQuery(
                                role.getId()));

        assertThat(response)
                .isNotNull();

        assertThat(response.id())
                .isEqualTo(
                        role.getId().getValue());

        assertThat(response.name())
                .isEqualTo("ADMIN");

        verify(roleRepository)
                .findById(role.getId());
    }

    @Test
    void shouldThrowExceptionWhenGettingUnknownRole() {

        RoleId roleId =
                RoleId.generate();

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () ->
                        roleApplicationService.get(
                                new GetRoleQuery(
                                        roleId)))
                .isInstanceOf(
                        RoleNotFoundException.class);

        verify(roleRepository)
                .findById(roleId);
    }

    @Test
    void shouldGetAllRoles() {

        Role admin =
                createRole(
                        "ADMIN",
                        "Administrator role",
                        true);

        Role user =
                createRole(
                        "USER",
                        "Standard user role",
                        false);

        when(roleRepository.findAll())
                .thenReturn(
                        List.of(
                                admin,
                                user));

        List<RoleSummary> roles =
                roleApplicationService.getAll(
                        new GetRolesQuery());

        assertThat(roles)
                .hasSize(2)
                .extracting(
                        RoleSummary::name)
                .containsExactly(
                        "ADMIN",
                        "USER");

        verify(roleRepository)
                .findAll();
    }

    @Test
    void shouldGetActiveRoles() {

        Role admin =
                createRole(
                        "ADMIN",
                        "Administrator role",
                        true);

        Role user =
                createRole(
                        "USER",
                        "Standard user role",
                        false);

        when(roleRepository.findByStatus(
                RoleStatus.ACTIVE))
                .thenReturn(
                        List.of(
                                admin,
                                user));

        List<RoleSummary> roles =
                roleApplicationService.getActive(
                        new GetActiveRolesQuery());

        assertThat(roles)
                .hasSize(2);

        assertThat(roles)
                .allSatisfy(
                        role ->
                                assertThat(role.status())
                                        .isEqualTo(
                                                RoleStatus.ACTIVE));

        verify(roleRepository)
                .findByStatus(
                        RoleStatus.ACTIVE);
    }

    @Test
    void shouldUpdateRole() {

        Role role =
                createRole(
                        "USER",
                        "Old description",
                        false);

        RoleName newName =
                RoleName.of("EDITOR");

        RoleDescription newDescription =
                RoleDescription.of(
                        "Updated description");

        UpdateRoleCommand command =
                new UpdateRoleCommand(
                        role.getId(),
                        newName,
                        newDescription);

        when(roleRepository.findById(
                role.getId()))
                .thenReturn(
                        Optional.of(role));

        when(roleRepository.save(role))
                .thenReturn(role);

        RoleResponse response =
                roleApplicationService.update(command);

        assertThat(response.name())
                .isEqualTo("EDITOR");

        assertThat(response.description())
                .isEqualTo(
                        "Updated description");

        verify(roleDomainService)
                .validateRename(
                        role.getId(),
                        newName);

        verify(roleRepository)
                .save(role);
    }

    @Test
    void shouldActivateRole() {

        Role role =
                createInactiveRole(
                        "USER",
                        "Standard user role",
                        false);

        when(roleRepository.findById(
                role.getId()))
                .thenReturn(
                        Optional.of(role));

        when(roleRepository.save(role))
                .thenReturn(role);

        RoleResponse response =
                roleApplicationService.activate(
                        new ActivateRoleCommand(
                                role.getId()));

        assertThat(response.status())
                .isEqualTo(
                        RoleStatus.ACTIVE);

        verify(roleRepository)
                .save(role);
    }

    @Test
    void shouldDeactivateRole() {

        Role role =
                createRole(
                        "USER",
                        "Standard user role",
                        false);

        when(roleRepository.findById(
                role.getId()))
                .thenReturn(
                        Optional.of(role));

        when(roleRepository.save(role))
                .thenReturn(role);

        RoleResponse response =
                roleApplicationService.deactivate(
                        new DeactivateRoleCommand(
                                role.getId()));

        assertThat(response.status())
                .isEqualTo(
                        RoleStatus.INACTIVE);

        verify(roleRepository)
                .save(role);
    }

    @Test
    void shouldDeleteRole() {

        Role role =
                createRole(
                        "USER",
                        "Standard user role",
                        false);

        when(roleRepository.findById(
                role.getId()))
                .thenReturn(
                        Optional.of(role));

        roleApplicationService.delete(
                new DeleteRoleCommand(
                        role.getId()));

        verify(roleRepository)
                .delete(role.getId());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingUnknownRole() {

        RoleId roleId =
                RoleId.generate();

        UpdateRoleCommand command =
                new UpdateRoleCommand(
                        roleId,
                        RoleName.of("EDITOR"),
                        RoleDescription.of(
                                "Updated description"));

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () ->
                        roleApplicationService.update(command))
                .isInstanceOf(
                        RoleNotFoundException.class);

        verify(roleRepository)
                .findById(roleId);

        verifyNoInteractions(
                roleDomainService);
    }

    private Role createRole(
            String name,
            String description,
            boolean system) {

        return Role.create(
                RoleName.of(name),
                RoleDescription.of(description),
                system);
    }

    private Role createInactiveRole(
            String name,
            String description,
            boolean system) {

        return Role.restore(
                RoleId.generate(),
                RoleName.of(name),
                RoleDescription.of(description),
                RoleStatus.INACTIVE,
                system,
                java.time.Instant.now(),
                java.time.Instant.now());
    }
}
