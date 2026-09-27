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


    @Query("""
        SELECT vc.observation.id, COUNT(vc)
        FROM ValidationCommentEntity vc
        WHERE vc.observation.dataCollection.id = :dataCollectionId
        GROUP BY vc.observation.id
        """)
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


    @Query(value = """
        SELECT
            EXTRACT(MONTH FROM vc.created_at)::INTEGER AS month,
            TO_CHAR(vc.created_at, 'Mon') AS month_name,
            COUNT(*) AS total
        FROM validation_comment vc
        WHERE vc.campaign_id = :campaignId
          AND vc.reference_year = :referenceYear
        GROUP BY month, month_name
        ORDER BY month
        """, nativeQuery = true)
    List<Object[]> findValidationTrend(
            @Param("campaignId") Long campaignId,
            @Param("referenceYear") Integer referenceYear);



    @Query(value = """
        SELECT
            COUNT(*) AS total_comments,
            COUNT(*) FILTER (
                WHERE vc.severity = 'CRITICAL'
            ) AS critical_comments,
            COUNT(DISTINCT vc.questionnaire_variable_id)
                AS affected_variables,
            COUNT(DISTINCT qv.questionnaire_id)
                AS affected_questionnaires
        FROM validation_comment vc
        JOIN questionnaire_variable qv
            ON qv.id = vc.questionnaire_variable_id
        WHERE vc.campaign_id = :campaignId
          AND vc.reference_year = :referenceYear
        """, nativeQuery = true)
    Object[] findKpiSummary(
            @Param("campaignId") Long campaignId,
            @Param("referenceYear") Integer referenceYear);


}