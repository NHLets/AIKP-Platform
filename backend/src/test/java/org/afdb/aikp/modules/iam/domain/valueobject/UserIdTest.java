package org.afdb.aikp.modules.iam.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIdTest {

    @Test
    void shouldCreateUserIdFromUuid() {

        UUID value =
                UUID.randomUUID();

        UserId userId =
                UserId.of(value);

        assertThat(userId.getValue())
                .isEqualTo(value);
    }

    @Test
    void shouldGenerateUserId() {

        UserId userId =
                UserId.generate();

        assertThat(userId.getValue())
                .isNotNull();
    }

    @Test
    void shouldRejectNullUuid() {

        assertThatThrownBy(
                () -> UserId.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBeEqualForSameUuid() {

        UUID value =
                UUID.randomUUID();

        UserId first =
                UserId.of(value);

        UserId second =
                UserId.of(value);

        assertThat(first)
                .isEqualTo(second);

        assertThat(first.hashCode())
                .isEqualTo(second.hashCode());
    }
}
