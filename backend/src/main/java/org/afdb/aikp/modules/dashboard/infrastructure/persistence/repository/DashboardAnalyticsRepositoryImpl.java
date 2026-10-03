package org.afdb.aikp.modules.dashboard.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.afdb.aikp.modules.dashboard.domain.repository.DashboardAnalyticsRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardAnalyticsRepositoryImpl
        implements DashboardAnalyticsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Object[] findKpiSummary(
            UUID campaignId,
            Integer referenceYear) {

        String sql = """
            SELECT
                COUNT(*) AS total_comments,
                COUNT(*) FILTER (
                    WHERE vc.severity = 'CRITICAL'
                ) AS critical_comments,
                COUNT(DISTINCT o.questionnaire_variable_id)
                    AS affected_variables,
                COUNT(DISTINCT qv.questionnaire_id)
                    AS affected_questionnaires
            FROM reference.validation_comment vc
            JOIN reference.data_collection_observation o
              ON o.id = vc.observation_id
            JOIN reference.data_collection dc
              ON dc.id = o.data_collection_id
            JOIN metadata.questionnaire_variable qv
              ON qv.id = o.questionnaire_variable_id
            WHERE dc.campaign_id = :campaignId
              AND o.reference_year = :referenceYear
            """;

        return (Object[]) entityManager
                .createNativeQuery(sql)
                .setParameter("campaignId", campaignId)
                .setParameter("referenceYear", referenceYear)
                .getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findSeverityDistribution(
            UUID campaignId,
            Integer referenceYear) {

        String sql = """
            SELECT
                vc.severity,
                COUNT(*) AS count
            FROM reference.validation_comment vc
            JOIN reference.data_collection_observation o
              ON o.id = vc.observation_id
            JOIN reference.data_collection dc
              ON dc.id = o.data_collection_id
            WHERE dc.campaign_id = :campaignId
              AND o.reference_year = :referenceYear
            GROUP BY vc.severity
            ORDER BY vc.severity
            """;

        return entityManager
                .createNativeQuery(sql)
                .setParameter("campaignId", campaignId)
                .setParameter("referenceYear", referenceYear)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findValidationTrend(
            UUID campaignId,
            Integer referenceYear) {

        String sql = """
            SELECT
                TO_CHAR(dcv.validated_at, 'YYYY-MM') AS period,
                COUNT(*) FILTER (
                    WHERE dcv.decision = 'VALIDATED'
                ) AS validated,
                COUNT(*) FILTER (
                    WHERE dcv.decision = 'REJECTED'
                ) AS rejected
            FROM reference.data_collection_validation dcv
            JOIN reference.data_collection dc
              ON dc.id = dcv.data_collection_id
            WHERE dc.campaign_id = :campaignId
              AND EXISTS (
                  SELECT 1
                  FROM reference.data_collection_observation o
                  WHERE o.data_collection_id = dc.id
                    AND o.reference_year = :referenceYear
              )
            GROUP BY period
            ORDER BY period
            """;

        return entityManager
                .createNativeQuery(sql)
                .setParameter("campaignId", campaignId)
                .setParameter("referenceYear", referenceYear)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findValidationHeatmap(
            UUID campaignId,
            Integer referenceYear) {

        String sql = """
            SELECT
                q.code AS questionnaire,
                qv.series_code AS variable,
                COUNT(*) AS count
            FROM reference.validation_comment vc
            JOIN reference.data_collection_observation o
              ON o.id = vc.observation_id
            JOIN reference.data_collection dc
              ON dc.id = o.data_collection_id
            JOIN metadata.questionnaire_variable qv
              ON qv.id = o.questionnaire_variable_id
            JOIN metadata.questionnaire q
              ON q.id = qv.questionnaire_id
            WHERE dc.campaign_id = :campaignId
              AND o.reference_year = :referenceYear
            GROUP BY q.code, qv.series_code
            ORDER BY q.code, qv.series_code
            """;

        return entityManager
                .createNativeQuery(sql)
                .setParameter("campaignId", campaignId)
                .setParameter("referenceYear", referenceYear)
                .getResultList();
    }
}
