package org.afdb.aikp.shared.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class BaseEntityTest {

    @Test
    void shouldBeNewWhenCreated() {

        TestEntity entity =
                new TestEntity(UUID.randomUUID());

        assertThat(entity.isNew())
                .isTrue();
    }

    @Test
    void shouldMarkEntityAsExistingAfterPersist() {

        TestEntity entity =
                new TestEntity(UUID.randomUUID());

        assertThat(entity.isNew())
                .isTrue();

        entity.onPersist();

        assertThat(entity.isNew())
                .isFalse();
    }

    @Test
    void shouldMarkEntityAsNewAgainWhenRequested() {

        TestEntity entity =
                new TestEntity(UUID.randomUUID());

        entity.onPersist();

        assertThat(entity.isNew())
                .isFalse();

        entity.markNew();

        assertThat(entity.isNew())
                .isTrue();
    }

    private static final class TestEntity
            extends BaseEntity {

        private TestEntity(UUID id) {
            super(id);
        }
    }
}
