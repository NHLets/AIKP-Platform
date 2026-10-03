package org.afdb.aikp.modules.dashboard.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.afdb.aikp.modules.dashboard.domain.repository.DashboardAnalyticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@Transactional
class DashboardAnalyticsRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine")
                    .withDatabaseName("aikp")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void configureDatabase(
            DynamicPropertyRegistry registry) {

        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl);

        registry.add(
                "spring.datasource.username",
                postgres::getUsername);

        registry.add(
                "spring.datasource.password",
                postgres::getPassword);

        registry.add(
                "spring.datasource.driver-class-name",
                postgres::getDriverClassName);
    }

    @Autowired
    private DashboardAnalyticsRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    private UUID campaignId;
    private UUID questionnaireId;
    private String questionnaireCode;
    private UUID questionnaireVariableId;
    private UUID countryId;
    private UUID organizationId;
    private UUID personId;
    private UUID dataCollectionId;
    private UUID observationId;
    private UUID secondObservationId;

    private static final int REFERENCE_YEAR = 2025;

    @BeforeEach
    void setUp() {

        campaignId = UUID.randomUUID();
        questionnaireId = UUID.randomUUID();
        questionnaireCode = "AN-PW-B-" + UUID.randomUUID().toString().substring(0, 8);
        questionnaireVariableId = UUID.randomUUID();
        countryId = UUID.randomUUID();
        organizationId = UUID.randomUUID();
        personId = UUID.randomUUID();
        dataCollectionId = UUID.randomUUID();
        observationId = UUID.randomUUID();
        secondObservationId = UUID.randomUUID();

        insertCountry();
        insertOrganization();
        insertPerson();
        insertCampaign();
        insertQuestionnaire();
        insertQuestionnaireVariable();
        insertDataCollection();
        insertObservations();
        insertValidationComments();
        insertValidations();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void shouldFindKpiSummary() {

        Object[] result =
                repository.findKpiSummary(
                        campaignId,
                        REFERENCE_YEAR);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);

        assertThat(((Number) result[0]).longValue())
                .isEqualTo(3L);

        assertThat(((Number) result[1]).longValue())
                .isEqualTo(1L);

        assertThat(((Number) result[2]).longValue())
                .isEqualTo(2L);

        assertThat(((Number) result[3]).longValue())
                .isEqualTo(1L);
    }

    @Test
    void shouldFindSeverityDistribution() {

        List<Object[]> result =
                repository.findSeverityDistribution(
                        campaignId,
                        REFERENCE_YEAR);

        assertThat(result)
                .hasSize(3);

        assertThat(result)
                .extracting(row -> row[0].toString())
                .containsExactly(
                        "CRITICAL",
                        "HIGH",
                        "MEDIUM");

        assertThat(result)
                .extracting(row -> ((Number) row[1]).longValue())
                .containsExactly(
                        1L,
                        1L,
                        1L);
    }

    @Test
    void shouldFindValidationTrend() {

        List<Object[]> result =
                repository.findValidationTrend(
                        campaignId,
                        REFERENCE_YEAR);

        assertThat(result)
                .hasSize(2);

        assertThat(result)
                .extracting(row -> row[0].toString())
                .containsExactly(
                        "2025-01",
                        "2025-02");

        assertThat(result.get(0)[1])
                .extracting(value -> ((Number) value).longValue())
                .isEqualTo(1L);

        assertThat(result.get(0)[2])
                .extracting(value -> ((Number) value).longValue())
                .isEqualTo(1L);

        assertThat(result.get(1)[1])
                .extracting(value -> ((Number) value).longValue())
                .isEqualTo(1L);

        assertThat(result.get(1)[2])
                .extracting(value -> ((Number) value).longValue())
                .isEqualTo(0L);
    }

    @Test
    void shouldFindValidationHeatmap() {

        List<Object[]> result =
                repository.findValidationHeatmap(
                        campaignId,
                        REFERENCE_YEAR);

        assertThat(result)
                .hasSize(2);

        assertThat(result)
                .extracting(row -> row[0].toString())
                .containsExactly(
                        questionnaireCode,
                        questionnaireCode);

        assertThat(result)
                .extracting(row -> row[1].toString())
                .containsExactly(
                        "B001",
                        "B002");

        assertThat(result)
                .extracting(row -> ((Number) row[2]).longValue())
                .containsExactly(
                        2L,
                        1L);
    }

    private void insertCountry() {

        entityManager.createNativeQuery("""
            INSERT INTO reference.country
                (id, iso2_code, iso3_code, numeric_code,
                 name, official_name, created_at)
            VALUES
                (:id, 'ZZ', 'ZZZ', '999',
                 'Analytics Test Country',
                 'Analytics Test Country',
                 CURRENT_TIMESTAMP)
            """)
                .setParameter("id", countryId)
                .executeUpdate();
    }

    private void insertOrganization() {

        entityManager.createNativeQuery("""
            INSERT INTO reference.organization
                (id, code, name, type, country_id, created_at)
            VALUES
                (:id, :code, :name, 'GOVERNMENT_AGENCY', :countryId,
                 CURRENT_TIMESTAMP)
            """)
                .setParameter("id", organizationId)
                .setParameter("code", "AN-" + shortId())
                .setParameter("name", "Analytics Test Organization")
                .setParameter("countryId", countryId)
                .executeUpdate();
    }

    private void insertPerson() {

        entityManager.createNativeQuery("""
            INSERT INTO reference.person
                (id, full_name, organization_id)
            VALUES
                (:id, :name, :organizationId)
            """)
                .setParameter("id", personId)
                .setParameter("name", "Analytics Test Person")
                .setParameter("organizationId", organizationId)
                .executeUpdate();
    }

    private void insertCampaign() {

        entityManager.createNativeQuery("""
            INSERT INTO campaign.campaign
                (id, code, name, description,
                 start_date, end_date, status, created_at)
            VALUES
                (:id, :code, :name, :description,
                 DATE '2025-01-01',
                 DATE '2025-12-31',
                 'ACTIVE',
                 CURRENT_TIMESTAMP)
            """)
                .setParameter("id", campaignId)
                .setParameter("code", "AN-" + shortId())
                .setParameter("name", "Analytics Test Campaign")
                .setParameter("description", "Analytics repository integration test")
                .executeUpdate();
    }

    private void insertQuestionnaire() {

        entityManager.createNativeQuery("""
            INSERT INTO metadata.questionnaire
                (id, code, name, description,
                 questionnaire_version, default_language,
                 status, render_type, active, created_at)
            VALUES
                (:id, :code, 'Power B',
                 'Analytics Test Questionnaire',
                 '1.0', 'en',
                 'ACTIVE', 'FORM', TRUE, CURRENT_TIMESTAMP)
            """)
                .setParameter("id", questionnaireId)
                .setParameter("code", questionnaireCode)
                .executeUpdate();
    }

    private void insertQuestionnaireVariable() {

        UUID variable2Id = UUID.randomUUID();

        entityManager.createNativeQuery("""
            INSERT INTO metadata.questionnaire_variable
                (id, questionnaire_id, questionnaire_group_id,
                 series_code, name, definition, data_type,
                 unit, required, display_order, active,
                 created_at, version)
            VALUES
                (:id, :questionnaireId, NULL,
                 'B001', 'Variable B001', NULL, 'NUMBER',
                 'MW', TRUE, 1, TRUE,
                 CURRENT_TIMESTAMP, 0),
                (:id2, :questionnaireId, NULL,
                 'B002', 'Variable B002', NULL, 'NUMBER',
                 'MW', TRUE, 2, TRUE,
                 CURRENT_TIMESTAMP, 0)
            """)
                .setParameter("id", questionnaireVariableId)
                .setParameter("id2", variable2Id)
                .setParameter("questionnaireId", questionnaireId)
                .executeUpdate();

        secondObservationId = UUID.randomUUID();

        entityManager.createNativeQuery("""
            UPDATE reference.data_collection_observation
            SET questionnaire_variable_id = :variableId
            WHERE id = :observationId
            """)
                .setParameter("variableId", variable2Id)
                .setParameter("observationId", secondObservationId)
                .executeUpdate();
    }

    private void insertDataCollection() {

        entityManager.createNativeQuery("""
            INSERT INTO reference.data_collection
                (id, campaign_id, country_id, questionnaire_id,
                 responsible_organization_id, data_collector_id,
                 status)
            VALUES
                (:id, :campaignId, :countryId, :questionnaireId,
                 :organizationId, :personId, 'SUBMITTED')
            """)
                .setParameter("id", dataCollectionId)
                .setParameter("campaignId", campaignId)
                .setParameter("countryId", countryId)
                .setParameter("questionnaireId", questionnaireId)
                .setParameter("organizationId", organizationId)
                .setParameter("personId", personId)
                .executeUpdate();
    }

    private void insertObservations() {

        entityManager.createNativeQuery("""
            INSERT INTO reference.data_collection_observation
                (id, data_collection_id,
                 questionnaire_variable_id,
                 reference_year,
                 numeric_value,
                 observation_status,
                 created_at,
                 version)
            VALUES
                (:id, :collectionId, :variableId,
                 :year, 100, 'PROVIDED',
                 CURRENT_TIMESTAMP, 0)
            """)
                .setParameter("id", observationId)
                .setParameter("collectionId", dataCollectionId)
                .setParameter("variableId", questionnaireVariableId)
                .setParameter("year", REFERENCE_YEAR)
                .executeUpdate();

        entityManager.createNativeQuery("""
            INSERT INTO reference.data_collection_observation
                (id, data_collection_id,
                 questionnaire_variable_id,
                 reference_year,
                 numeric_value,
                 observation_status,
                 created_at,
                 version)
            VALUES
                (:id, :collectionId,
                 (SELECT id
                    FROM metadata.questionnaire_variable
                   WHERE questionnaire_id = :questionnaireId
                     AND series_code = 'B002'),
                 :year, 200, 'PROVIDED',
                 CURRENT_TIMESTAMP, 0)
            """)
                .setParameter("id", secondObservationId)
                .setParameter("collectionId", dataCollectionId)
                .setParameter("questionnaireId", questionnaireId)
                .setParameter("year", REFERENCE_YEAR)
                .executeUpdate();
    }

    private void insertValidationComments() {

        insertComment("CRITICAL", observationId);
        insertComment("HIGH", observationId);
        insertComment("MEDIUM", secondObservationId);
    }

    private void insertComment(
            String severity,
            UUID targetObservationId) {

        UUID commentId = UUID.randomUUID();

        entityManager.createNativeQuery("""
            INSERT INTO reference.validation_comment
                (id, observation_id, validator_id,
                 comment, severity, created_at, updated_at)
            VALUES
                (:id, :observationId, :validatorId,
                 :comment, :severity,
                 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """)
                .setParameter("id", commentId)
                .setParameter("observationId", targetObservationId)
                .setParameter("validatorId", personId)
                .setParameter("comment", "Analytics test comment")
                .setParameter("severity", severity)
                .executeUpdate();
    }

    private void insertValidations() {

        insertValidation(
                "VALIDATED",
                "2025-01-15T10:00:00Z");

        insertValidation(
                "REJECTED",
                "2025-01-15T11:00:00Z");

        insertValidation(
                "VALIDATED",
                "2025-02-15T10:00:00Z");
    }

    private void insertValidation(
            String decision,
            String timestamp) {

        entityManager.createNativeQuery("""
            INSERT INTO reference.data_collection_validation
                (id, data_collection_id, validator_id,
                 decision, comments, validated_at)
            VALUES
                (:id, :collectionId, :validatorId,
                 :decision, :comments,
                 :validatedAt)
            """)
                .setParameter("id", UUID.randomUUID())
                .setParameter("collectionId", dataCollectionId)
                .setParameter("validatorId", personId)
                .setParameter("decision", decision)
                .setParameter("comments", "Analytics test validation")
                .setParameter("validatedAt", Instant.parse(timestamp))
                .executeUpdate();
    }

    private String shortId() {
        return UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}
