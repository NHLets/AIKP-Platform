package org.afdb.aikp.modules.iam.role.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoleIdTest {

    @Test
    void shouldCreateRoleIdFromUuid() {

        UUID uuid = UUID.randomUUID();

        RoleId roleId = RoleId.of(uuid);

        assertNotNull(roleId);
        assertEquals(uuid, roleId.getValue());
    }

    @Test
    void shouldGenerateRoleId() {

        RoleId roleId = RoleId.generate();

        assertNotNull(roleId);
        assertNotNull(roleId.getValue());
    }

    @Test
    void shouldRejectNullUuid() {

        assertThrows(
                NullPointerException.class,
                () -> RoleId.of(null));
    }

    @Test
    void shouldSupportEquality() {

        UUID uuid = UUID.randomUUID();

        RoleId first = RoleId.of(uuid);
        RoleId second = RoleId.of(uuid);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void shouldGenerateDifferentIds() {

        RoleId first = RoleId.generate();
        RoleId second = RoleId.generate();

        assertNotEquals(first, second);
    }
}
