package org.afdb.aikp.modules.iam.role.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleDescriptionTest {

    @Test
    void shouldCreateValidDescription() {

        RoleDescription description =
                RoleDescription.of(
                        "Manages AIKP platform administration.");

        assertEquals(
                "Manages AIKP platform administration.",
                description.value());
    }

    @Test
    void shouldNormalizeWhitespace() {

        RoleDescription description =
                RoleDescription.of(
                        "Manages    AIKP    platform");

        assertEquals(
                "Manages AIKP platform",
                description.value());
    }

    @Test
    void shouldRejectNullValue() {

        assertThrows(
                NullPointerException.class,
                () -> RoleDescription.of(null));
    }

    @Test
    void shouldRejectBlankValue() {

        assertThrows(
                IllegalArgumentException.class,
                () -> RoleDescription.of("   "));
    }

    @Test
    void shouldRejectTooShortValue() {

        assertThrows(
                IllegalArgumentException.class,
                () -> RoleDescription.of("A"));
    }

    @Test
    void shouldRejectTooLongValue() {

        String value = "A".repeat(501);

        assertThrows(
                IllegalArgumentException.class,
                () -> RoleDescription.of(value));
    }

    @Test
    void shouldSupportEquality() {

        RoleDescription first =
                RoleDescription.of("Administrator role");

        RoleDescription second =
                RoleDescription.of("Administrator role");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}
