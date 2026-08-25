package org.afdb.aikp.modules.iam.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsernameTest {

    @Test
    void shouldCreateTrimmedUsername() {

        Username username =
                Username.of("  john.doe  ");

        assertThat(username.value())
                .isEqualTo("john.doe");
    }

    @Test
    void shouldRejectNullUsername() {

        assertThatThrownBy(
                () -> Username.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectEmptyUsername() {

        assertThatThrownBy(
                () -> Username.of("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectUsernameShorterThanMinimumLength() {

        assertThatThrownBy(
                () -> Username.of("ab"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectUsernameLongerThanMaximumLength() {

        assertThatThrownBy(
                () -> Username.of(
                        "a".repeat(51)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldBeEqualForSameValue() {

        Username first =
                Username.of("john.doe");

        Username second =
                Username.of("john.doe");

        assertThat(first)
                .isEqualTo(second);

        assertThat(first.hashCode())
                .isEqualTo(second.hashCode());
    }

    @Test
    void shouldReturnValueFromToString() {

        Username username =
                Username.of("john.doe");

        assertThat(username.toString())
                .isEqualTo("john.doe");
    }
}
