package org.afdb.aikp.modules.dataCollection.domain.model;

import java.util.UUID;

public record ObservationId(UUID value) {

    public static ObservationId generate() {
        return new ObservationId(UUID.randomUUID());
    }
}
