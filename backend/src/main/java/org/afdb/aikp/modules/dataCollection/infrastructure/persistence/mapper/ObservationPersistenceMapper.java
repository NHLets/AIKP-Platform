package org.afdb.aikp.modules.dataCollection.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;
import org.afdb.aikp.modules.dataCollection.domain.model.*;
import org.afdb.aikp.modules.dataCollection.infrastructure.persistence.entity.ObservationEntity;

@Component
public class ObservationPersistenceMapper {

    public ObservationEntity toEntity(Observation domain) {
        ObservationEntity entity = new ObservationEntity();
        entity.setId(domain.getId().value());
        entity.setDataCollectionId(domain.getDataCollectionId());
        entity.setVariableId(domain.getVariableId());
        entity.setOrganizationId(domain.getOrganizationId());
        entity.setValue(domain.getValue());
        entity.setStatus(domain.getStatus());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public Observation toDomain(ObservationEntity entity) {
        Observation observation = new Observation(
            new ObservationId(entity.getId()),
            entity.getDataCollectionId(),
            entity.getVariableId(),
            entity.getOrganizationId(),
            entity.getValue()
        );

        if (entity.getStatus() == ObservationStatus.SUBMITTED) {
            observation.submit();
        } else if (entity.getStatus() == ObservationStatus.VALIDATED) {
            observation.submit();
            observation.validate();
        }

        return observation;
    }
}
