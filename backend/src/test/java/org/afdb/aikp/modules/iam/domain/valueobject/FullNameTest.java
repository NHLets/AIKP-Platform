package org.afdb.aikp.modules.iam.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FullNameTest {

    @Test
    void shouldCreateNormalizedFullName() {

        FullName fullName =
                FullName.of("  John    Paul   Doe  ");

        assertThat(fullName.value())
                .isEqualTo("John Paul Doe");
    }

    @Test
    void shouldRejectNullFullName() {

        assertThatThrownBy(
                () -> FullName.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectEmptyFullName() {

        assertThatThrownBy(
                () -> FullName.of("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectFullNameShorterThanMinimumLength() {

        assertThatThrownBy(
                () -> FullName.of("A"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectFullNameLongerThanMaximumLength() {

        assertThatThrownBy(
                () -> FullName.of(
                        "a".repeat(151)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldBeEqualForSameValue() {

        FullName first =
                FullName.of("John Doe");

        FullName second =
                FullName.of("John Doe");

        assertThat(first)
                .isEqualTo(second);

        assertThat(first.hashCode())
                .isEqualTo(second.hashCode());
    }

    @Test
    void shouldReturnValueFromToString() {

        FullName fullName =
                FullName.of("John Doe");

        assertThat(fullName.toString())
                .isEqualTo("John Doe");
    }
}
