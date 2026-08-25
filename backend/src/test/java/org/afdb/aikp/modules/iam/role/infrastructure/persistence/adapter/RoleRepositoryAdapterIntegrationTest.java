package org.afdb.aikp.modules.iam.role.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.role.domain.repository.RoleRepository;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace =
                AutoConfigureTestDatabase.Replace.NONE)
@Import(RoleRepositoryAdapter.class)
class RoleRepositoryAdapterIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;


    @Test
    void shouldSaveAndFindRoleById() {

        Role role =
                createRole(
                        "ROLE_SAVE_"
                                + randomSuffix());

        Role saved =
                roleRepository.save(role);

        assertThat(saved)
                .isNotNull();

        assertThat(saved.getId())
                .isEqualTo(role.getId());

        assertThat(
                roleRepository.findById(
                        saved.getId()))
                .isPresent()
                .get()
                .satisfies(found -> {

                    assertThat(found.getId())
                            .isEqualTo(
                                    saved.getId());

                    assertThat(found.getName())
                            .isEqualTo(
                                    saved.getName());
                });
    }


    @Test
    void shouldFindRoleByName() {

        Role role =
                roleRepository.save(
                        createRole(
                                "ROLE_NAME_"
                                        + randomSuffix()));

        assertThat(
                roleRepository.findByName(
                        role.getName()))
                .isPresent()
                .get()
                .satisfies(found -> {

                    assertThat(found.getId())
                            .isEqualTo(
                                    role.getId());

                    assertThat(found.getName())
                            .isEqualTo(
                                    role.getName());
                });
    }


    @Test
    void shouldReturnEmptyWhenRoleIsNotFoundById() {

        RoleId unknownId =
                RoleId.of(
                        UUID.randomUUID());

        assertThat(
                roleRepository.findById(
                        unknownId))
                .isEmpty();
    }


    @Test
    void shouldReturnEmptyWhenRoleIsNotFoundByName() {

        RoleName unknownName =
                RoleName.of(
                        "UNKNOWN_ROLE_"
                                + randomSuffix());

        assertThat(
                roleRepository.findByName(
                        unknownName))
                .isEmpty();
    }


    @Test
    void shouldFindAllRoles() {

        Role first =
                roleRepository.save(
                        createRole(
                                "ROLE_ALL_A_"
                                        + randomSuffix()));

        Role second =
                roleRepository.save(
                        createRole(
                                "ROLE_ALL_B_"
                                        + randomSuffix()));

        List<Role> roles =
                roleRepository.findAll();

        assertThat(roles)
                .extracting(
                        Role::getId)
                .contains(
                        first.getId(),
                        second.getId());
    }


    @Test
    void shouldFindRolesByStatus() {

        Role activeRole =
                roleRepository.save(
                        createRole(
                                "ROLE_ACTIVE_"
                                        + randomSuffix()));

        Role inactiveRole =
                createRole(
                        "ROLE_INACTIVE_"
                                + randomSuffix());

        inactiveRole.deactivate();

        inactiveRole =
                roleRepository.save(
                        inactiveRole);

        List<Role> activeRoles =
                roleRepository.findByStatus(
                        RoleStatus.ACTIVE);

        List<Role> inactiveRoles =
                roleRepository.findByStatus(
                        RoleStatus.INACTIVE);

        assertThat(activeRoles)
                .extracting(
                        Role::getId)
                .contains(
                        activeRole.getId());

        assertThat(inactiveRoles)
                .extracting(
                        Role::getId)
                .contains(
                        inactiveRole.getId());
    }


    @Test
    void shouldCheckRoleExistenceById() {

        Role saved =
                roleRepository.save(
                        createRole(
                                "ROLE_EXISTS_ID_"
                                        + randomSuffix()));

        assertThat(
                roleRepository.existsById(
                        saved.getId()))
                .isTrue();

        assertThat(
                roleRepository.existsById(
                        RoleId.of(
                                UUID.randomUUID())))
                .isFalse();
    }


    @Test
    void shouldCheckRoleExistenceByName() {

        Role saved =
                roleRepository.save(
                        createRole(
                                "ROLE_EXISTS_NAME_"
                                        + randomSuffix()));

        assertThat(
                roleRepository.existsByName(
                        saved.getName()))
                .isTrue();

        assertThat(
                roleRepository.existsByName(
                        RoleName.of(
                                "UNKNOWN_ROLE_"
                                        + randomSuffix())))
                .isFalse();
    }


    @Test
    void shouldDeleteRole() {

        Role saved =
                roleRepository.save(
                        createRole(
                                "ROLE_DELETE_"
                                        + randomSuffix()));

        RoleId roleId =
                saved.getId();

        assertThat(
                roleRepository.existsById(
                        roleId))
                .isTrue();

        roleRepository.delete(
                roleId);

        assertThat(
                roleRepository.existsById(
                        roleId))
                .isFalse();
    }


    private Role createRole(
            String roleName) {

        return Role.create(
                RoleName.of(roleName),
                RoleDescription.of(
                        "Integration test role"),
                false);
    }


    private String randomSuffix() {

        return UUID.randomUUID()
                .toString()
                .replace(
                        "-",
                        "")
                .substring(
                        0,
                        12);
    }
}
