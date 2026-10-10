package org.afdb.aikp.modules.quality.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public final class QualityFindingId {

    private final UUID value;

    private QualityFindingId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    public static QualityFindingId generate() {
        return new QualityFindingId(UUID.randomUUID());
    }

    public static QualityFindingId of(UUID value) {
        return new QualityFindingId(value);
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof QualityFindingId id && value.equals(id.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
