package org.afdb.aikp.modules.quality.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public final class QualityRunId {

    private final UUID value;

    private QualityRunId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    public static QualityRunId generate() {
        return new QualityRunId(UUID.randomUUID());
    }

    public static QualityRunId of(UUID value) {
        return new QualityRunId(value);
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof QualityRunId id && value.equals(id.value);
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
