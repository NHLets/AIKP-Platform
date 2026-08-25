package org.afdb.aikp.modules.iam.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordHashTest {

    @Test
    void shouldCreatePasswordHash() {

        String hash =
                validPasswordHash();

        PasswordHash passwordHash =
                PasswordHash.of(hash);

        assertThat(passwordHash.value())
                .isEqualTo(hash);
    }

    @Test
    void shouldTrimPasswordHash() {

        String hash =
                validPasswordHash();

        PasswordHash passwordHash =
                PasswordHash.of(
                        "  " + hash + "  ");

        assertThat(passwordHash.value())
                .isEqualTo(hash);
    }

    @Test
    void shouldRejectNullPasswordHash() {

        assertThatThrownBy(
                () -> PasswordHash.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectEmptyPasswordHash() {

        assertThatThrownBy(
                () -> PasswordHash.of("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectPasswordHashShorterThanMinimumLength() {

        assertThatThrownBy(
                () -> PasswordHash.of(
                        "a".repeat(59)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectPasswordHashLongerThanMaximumLength() {

        assertThatThrownBy(
                () -> PasswordHash.of(
                        "a".repeat(256)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldBeEqualForSameValue() {

        String hash =
                validPasswordHash();

        PasswordHash first =
                PasswordHash.of(hash);

        PasswordHash second =
                PasswordHash.of(hash);

        assertThat(first)
                .isEqualTo(second);

        assertThat(first.hashCode())
                .isEqualTo(second.hashCode());
    }

    @Test
    void shouldHidePasswordHashInToString() {

        PasswordHash passwordHash =
                PasswordHash.of(
                        validPasswordHash());

        assertThat(passwordHash.toString())
                .isEqualTo("********");
    }

    private String validPasswordHash() {

        return "a".repeat(60);
    }
}
