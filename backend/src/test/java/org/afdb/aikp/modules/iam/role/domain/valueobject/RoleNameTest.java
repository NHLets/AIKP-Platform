package org.afdb.aikp.modules.iam.role.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleNameTest {

    @Test
    void shouldCreateValidRoleName() {

        RoleName name = RoleName.of("  Data Administrator  ");

        assertEquals("Data Administrator", name.value());
    }

    @Test
    void shouldNormalizeWhitespace() {

        RoleName name =
                RoleName.of("Data    Collection    Manager");

        assertEquals(
                "Data Collection Manager",
                name.value());
    }

    @Test
    void shouldRejectNullValue() {

        assertThrows(
                NullPointerException.class,
                () -> RoleName.of(null));
    }

    @Test
    void shouldRejectBlankValue() {

        assertThrows(
                IllegalArgumentException.class,
                () -> RoleName.of("   "));
    }

    @Test
    void shouldRejectTooShortValue() {

        assertThrows(
                IllegalArgumentException.class,
                () -> RoleName.of("A"));
    }

    @Test
    void shouldRejectTooLongValue() {

        String value = "A".repeat(101);

        assertThrows(
                IllegalArgumentException.class,
                () -> RoleName.of(value));
    }

    @Test
    void shouldSupportEquality() {

        RoleName first =
                RoleName.of("Administrator");

        RoleName second =
                RoleName.of("Administrator");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}