package org.afdb.aikp.modules.collection.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionObservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for data collection observations.
 */
public interface DataCollectionObservationJpaRepository
        extends JpaRepository<DataCollectionObservationEntity, UUID> {

    List<DataCollectionObservationEntity>
            findByDataCollectionIdOrderByReferenceYearAsc(
                    UUID dataCollectionId);

    List<DataCollectionObservationEntity>
            findByQuestionnaireVariableIdOrderByReferenceYearAsc(
                    UUID questionnaireVariableId);

    Optional<DataCollectionObservationEntity>
            findByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                    UUID dataCollectionId,
                    UUID questionnaireVariableId,
                    int referenceYear);

    boolean existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
            UUID dataCollectionId,
            UUID questionnaireVariableId,
            int referenceYear);
}
