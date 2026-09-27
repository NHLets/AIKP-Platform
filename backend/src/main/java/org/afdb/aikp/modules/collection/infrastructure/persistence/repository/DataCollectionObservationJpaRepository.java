package org.afdb.aikp.modules.collection.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.query.Param;

import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionObservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.afdb.aikp.modules.validation.application.dto.QuestionnaireProgressDto;

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

    @Query("""
        SELECT COUNT(o)
        FROM DataCollectionObservationEntity o
        WHERE o.dataCollection.id = :dataCollectionId
          AND o.observationStatus = :status
        """)
    long countByDataCollectionAndObservationStatus(
        @Param("dataCollectionId") UUID dataCollectionId,
        @Param("status") String status
    );

    @Query("""
        SELECT COUNT(o)
        FROM DataCollectionObservationEntity o
        WHERE o.dataCollection.id = :dataCollectionId
        """)
    long countByDataCollection(
        @Param("dataCollectionId") UUID dataCollectionId
    );



    @Query(value = """
        SELECT
            q.code,
            q.name,
            SUM(CASE WHEN o.observation_status <> 'PENDING' THEN 1 ELSE 0 END),
            COUNT(*),
            ROUND(
                SUM(CASE WHEN o.observation_status <> 'PENDING' THEN 1 ELSE 0 END)
                * 100.0 / COUNT(*),
                1
            )
        FROM reference.data_collection_observation o
        JOIN reference.questionnaire_variable v
          ON v.id = o.questionnaire_variable_id
        JOIN reference.questionnaire q
          ON q.id = v.questionnaire_id
        WHERE o.data_collection_id = :dataCollectionId
        GROUP BY q.code, q.name
        ORDER BY q.code
        """, nativeQuery = true)
    java.util.List<Object[]> findQuestionnaireProgress(
        @Param("dataCollectionId") UUID dataCollectionId
    );



    @Query(value = """
        SELECT
            v.code,
            v.label,
            o.reference_year,
            COUNT(*)
        FROM reference.data_collection_observation o
        JOIN reference.questionnaire_variable v
          ON v.id = o.questionnaire_variable_id
        WHERE o.data_collection_id = :dataCollectionId
          AND o.observation_status = 'REJECTED'
        GROUP BY
            v.code,
            v.label,
            o.reference_year
        ORDER BY
            v.code,
            o.reference_year
        """, nativeQuery = true)
    java.util.List<Object[]> findValidationHeatmap(
        @Param("dataCollectionId") UUID dataCollectionId
    );



    @Query(value = """
        SELECT
            o.id,
            q.code,
            v.code,
            v.label,
            o.reference_year,
            COALESCE(vc.severity, 'INFO'),
            COALESCE(vc.comment, '')
        FROM reference.data_collection_observation o
        JOIN reference.questionnaire_variable v
          ON v.id = o.questionnaire_variable_id
        JOIN reference.questionnaire q
          ON q.id = v.questionnaire_id
        LEFT JOIN LATERAL (
            SELECT severity, comment
            FROM reference.validation_comment
            WHERE observation_id = o.id
            ORDER BY created_at DESC
            LIMIT 1
        ) vc ON TRUE
        WHERE o.data_collection_id = :dataCollectionId
          AND o.observation_status = 'REJECTED'
        ORDER BY
            q.code,
            v.code,
            o.reference_year
        """, nativeQuery = true)
    java.util.List<Object[]> findRejectedObservations(
        @Param("dataCollectionId") UUID dataCollectionId
    );

}