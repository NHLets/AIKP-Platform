package org.afdb.aikp.modules.validation.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.validation.infrastructure.persistence.entity.DataCollectionValidationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for DataCollectionValidationEntity.
 */
@Repository
public interface DataCollectionValidationJpaRepository
        extends JpaRepository<
                DataCollectionValidationEntity,
                UUID> {

    List<DataCollectionValidationEntity>
            findByDataCollectionId(
                    UUID dataCollectionId);
}
