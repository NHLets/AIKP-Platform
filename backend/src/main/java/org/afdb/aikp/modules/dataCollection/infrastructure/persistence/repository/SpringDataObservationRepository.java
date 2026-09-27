package org.afdb.aikp.modules.dataCollection.infrastructure.persistence.repository;

import org.afdb.aikp.modules.dataCollection.infrastructure.persistence.entity.ObservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataObservationRepository
        extends JpaRepository<ObservationEntity, UUID> {

    List<ObservationEntity> findByDataCollectionId(UUID dataCollectionId);

    List<ObservationEntity> findByOrganizationId(UUID organizationId);

    List<ObservationEntity> findByVariableId(UUID variableId);
}
