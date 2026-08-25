package org.afdb.aikp.modules.iam.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void shouldCreateNormalizedEmail() {

        Email email =
                Email.of("  John.Doe@Example.COM  ");

        assertThat(email.value())
                .isEqualTo("john.doe@example.com");
    }

    @Test
    void shouldRejectNullEmail() {

        assertThatThrownBy(
                () -> Email.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectEmptyEmail() {

        assertThatThrownBy(
                () -> Email.of("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectInvalidEmail() {

        assertThatThrownBy(
                () -> Email.of("invalid-email"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectEmailLongerThanMaximumLength() {

        String localPart =
                "a".repeat(250);

        String email =
                localPart + "@example.com";

        assertThatThrownBy(
                () -> Email.of(email))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldBeEqualForSameNormalizedValue() {

        Email first =
                Email.of("John.Doe@Example.com");

        Email second =
                Email.of("john.doe@example.com");

        assertThat(first)
                .isEqualTo(second);

        assertThat(first.hashCode())
                .isEqualTo(second.hashCode());
    }

    @Test
    void shouldReturnValueFromToString() {

        Email email =
                Email.of("john.doe@example.com");

        assertThat(email.toString())
                .isEqualTo("john.doe@example.com");
    }
}
