package org.afdb.aikp.modules.iam.role.domain.model;

import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleDescription;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void shouldCreateActiveRole() {

        Role role = Role.create(
                RoleName.of("Administrator"),
                RoleDescription.of(
                        "AIKP platform administrator"),
                false);

        assertNotNull(role.getId());
        assertEquals(
                RoleStatus.ACTIVE,
                role.getStatus());

        assertEquals(
                "Administrator",
                role.getName().value());

        assertFalse(role.isSystem());
        assertNotNull(role.getCreatedAt());
        assertNotNull(role.getUpdatedAt());
    }

    @Test
    void shouldActivateRole() {

        Role role = Role.create(
                RoleName.of("Enumerator"),
                RoleDescription.of(
                        "AIKP data collection enumerator"),
                false);

        role.deactivate();

        assertEquals(
                RoleStatus.INACTIVE,
                role.getStatus());

        role.activate();

        assertEquals(
                RoleStatus.ACTIVE,
                role.getStatus());
    }

    @Test
    void shouldDeactivateRole() {

        Role role = Role.create(
                RoleName.of("Reviewer"),
                RoleDescription.of(
                        "AIKP data reviewer"),
                false);

        role.deactivate();

        assertEquals(
                RoleStatus.INACTIVE,
                role.getStatus());
    }

    @Test
    void shouldRenameRole() {

        Role role = Role.create(
                RoleName.of("Reviewer"),
                RoleDescription.of(
                        "AIKP data reviewer"),
                false);

        role.rename(
                RoleName.of("Senior Reviewer"));

        assertEquals(
                "Senior Reviewer",
                role.getName().value());
    }

    @Test
    void shouldChangeDescription() {

        Role role = Role.create(
                RoleName.of("Reviewer"),
                RoleDescription.of(
                        "AIKP data reviewer"),
                false);

        role.changeDescription(
                RoleDescription.of(
                        "Senior AIKP data reviewer"));

        assertEquals(
                "Senior AIKP data reviewer",
                role.getDescription().value());
    }

    @Test
    void shouldCreateSystemRole() {

        Role role = Role.create(
                RoleName.of("System Administrator"),
                RoleDescription.of(
                        "Built-in system administrator"),
                true);

        assertTrue(role.isSystem());
    }
}