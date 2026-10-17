package org.afdb.aikp.modules.validation.infrastructure.persistence.repository;

import org.afdb.aikp.modules.validation.infrastructure.persistence.entity.ValidationCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.query.Param;

public interface ValidationCommentJpaRepository
        extends JpaRepository<ValidationCommentEntity, UUID> {

    List<ValidationCommentEntity> findByObservationIdOrderByCreatedAtAsc(
            UUID observationId);

    List<ValidationCommentEntity> findByValidatorIdOrderByCreatedAtDesc(
            UUID validatorId);


    @Query(value = """
        SELECT vc.observation_id, COUNT(*)
        FROM reference.validation_comment vc
        JOIN reference.data_collection_observation o
          ON o.id = vc.observation_id
        WHERE o.data_collection_id = :dataCollectionId
        GROUP BY vc.observation_id
        """, nativeQuery = true)
    List<Object[]> countByDataCollection(
        @Param("dataCollectionId") UUID dataCollectionId
    );



    @Query(value = """
        SELECT
            vc.severity,
            COUNT(*)
        FROM reference.validation_comment vc
        JOIN reference.data_collection_observation o
          ON o.id = vc.observation_id
        WHERE o.data_collection_id = :dataCollectionId
        GROUP BY vc.severity
        ORDER BY vc.severity
        """, nativeQuery = true)
    java.util.List<Object[]> findSeverityDistribution(
        @org.springframework.data.repository.query.Param("dataCollectionId")
        java.util.UUID dataCollectionId
    );



    @Query(value = """
        SELECT
            severity,
            COUNT(*)
        FROM reference.validation_comment
        WHERE observation_id IN (
            SELECT id
            FROM reference.data_collection_observation
            WHERE data_collection_id = :dataCollectionId
        )
        GROUP BY severity
        ORDER BY severity
        """, nativeQuery = true)
    java.util.List<Object[]> findSeverityStatistics(
        @Param("dataCollectionId") UUID dataCollectionId
    );




}
