package org.afdb.aikp.modules.quality.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityRunEntity;

public interface QualityRunJpaRepository
        extends JpaRepository<QualityRunEntity, UUID> {

    List<QualityRunEntity> findByDataCollectionIdOrderByStartedAtDesc(
            UUID dataCollectionId);

    Optional<QualityRunEntity> findFirstByDataCollectionIdOrderByStartedAtDesc(
            UUID dataCollectionId);
}
