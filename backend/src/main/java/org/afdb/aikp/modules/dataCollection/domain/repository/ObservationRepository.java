package org.afdb.aikp.modules.dataCollection.domain.repository;

import org.afdb.aikp.modules.dataCollection.domain.model.Observation;
import org.afdb.aikp.modules.dataCollection.domain.model.ObservationId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObservationRepository {

    Observation save(Observation observation);

    Optional<Observation> findById(ObservationId id);

    List<Observation> findByDataCollection(UUID dataCollectionId);

    List<Observation> findByOrganization(UUID organizationId);

    void delete(ObservationId id);
}
