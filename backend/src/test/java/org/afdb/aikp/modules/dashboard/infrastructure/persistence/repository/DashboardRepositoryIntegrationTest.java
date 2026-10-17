package org.afdb.aikp.modules.dashboard.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.afdb.aikp.modules.dashboard.application.response.DashboardResponse;
import org.afdb.aikp.modules.dashboard.domain.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@Transactional
class DashboardRepositoryIntegrationTest {

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
    private DashboardRepository dashboardRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private UUID campaignId;
    private UUID organizationId;
    private UUID personId;

    private UUID country1;
    private UUID country2;
    private UUID country3;
    private UUID country4;

    private UUID pwA;
    private UUID pwB;
    private UUID pwC;
    private UUID fg;

    private DashboardResponse dashboardBeforeTestData;

    @BeforeEach
    void setUp() {

        campaignId = UUID.randomUUID();
        organizationId = UUID.randomUUID();
        personId = UUID.randomUUID();

        country1 = UUID.randomUUID();
        country2 = UUID.randomUUID();
        country3 = UUID.randomUUID();
        country4 = UUID.randomUUID();

        pwA = UUID.randomUUID();
        pwB = UUID.randomUUID();
        pwC = UUID.randomUUID();
        fg = UUID.randomUUID();

        insertCampaign();

        dashboardBeforeTestData =
                dashboardRepository.getDashboard();

        insertCountries();
        insertOrganization();
        insertPerson();
        insertQuestionnaires();
        insertDataCollections();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void shouldCalculateCountryValidationKpisSeparatelyFromCollectionCounts() {

        DashboardResponse response =
                dashboardRepository.getDashboard();

        assertThat(response.totalCountries()
                - dashboardBeforeTestData.totalCountries())
                .isEqualTo(4);

        assertThat(response.fullyValidatedCountries()
                - dashboardBeforeTestData.fullyValidatedCountries())
                .isEqualTo(1);

        assertThat(response.countriesWithFgValidated()
                - dashboardBeforeTestData.countriesWithFgValidated())
                .isEqualTo(2);

        assertThat(response.countriesWithFgNotValidated()
                - dashboardBeforeTestData.countriesWithFgNotValidated())
                .isEqualTo(2);

        assertThat(
                response.countriesNotValidatedBecauseOnePwNotValidated()
                - dashboardBeforeTestData
                        .countriesNotValidatedBecauseOnePwNotValidated())
                .isEqualTo(3);

        /*
         * Country 1:
         * PW_A + PW_B + PW_C + F_G = VALIDATED
         *
         * Country 2:
         * PW_A + PW_B = VALIDATED, PW_C = SUBMITTED,
         * F_G = VALIDATED
         *
         * Country 3:
         * PW_A = VALIDATED, PW_B + PW_C = SUBMITTED,
         * F_G = SUBMITTED
         *
         * Country 4:
         * PW_A + PW_B + PW_C = SUBMITTED,
         * F_G absent.
         *
         * Thus there are 8 VALIDATED collections:
         * Country 1 -> 4
         * Country 2 -> 3
         * Country 3 -> 1
         */
        assertThat(response.validatedDataCollections()
                - dashboardBeforeTestData.validatedDataCollections())
                .isEqualTo(8);
    }

    private void insertCampaign() {

        entityManager.createNativeQuery("""
            INSERT INTO campaign.campaign
                (id, code, name, description,
                 start_date, end_date, status,
                 created_at, updated_at, version)
            VALUES
                (:id, 'DASH-TEST', 'Dashboard Test Campaign',
                 'Dashboard integration test',
                 :startDate, :endDate, 'ACTIVE',
                 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
            """)
                .setParameter("id", campaignId)
                .setParameter(
                        "startDate",
                        LocalDate.of(2025, 1, 1))
                .setParameter(
                        "endDate",
                        LocalDate.of(2026, 12, 31))
                .executeUpdate();
    }

    private void insertCountries() {

        insertCountry(
                country1, "AA", "AAA", "001",
                "Dashboard Country 1");

        insertCountry(
                country2, "BB", "BBB", "002",
                "Dashboard Country 2");

        insertCountry(
                country3, "CC", "CCC", "003",
                "Dashboard Country 3");

        insertCountry(
                country4, "DD", "DDD", "004",
                "Dashboard Country 4");
    }

    private void insertCountry(
            UUID id,
            String iso2,
            String iso3,
            String numericCode,
            String name) {

        entityManager.createNativeQuery("""
            INSERT INTO reference.country
                (id, iso2_code, iso3_code, numeric_code,
                 name, official_name, active,
                 created_at, updated_at, version)
            VALUES
                (:id, :iso2, :iso3, :numericCode,
                 :name, :name, true,
                 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
            """)
                .setParameter("id", id)
                .setParameter("iso2", iso2)
                .setParameter("iso3", iso3)
                .setParameter("numericCode", numericCode)
                .setParameter("name", name)
                .executeUpdate();
    }

    private void insertOrganization() {

        entityManager.createNativeQuery("""
            INSERT INTO reference.organization
                (id, code, name, type, country_id,
                 active, created_at, updated_at, version)
            VALUES
                (:id, 'DASH-ORG', 'Dashboard Test Organization',
                 'GOVERNMENT_AGENCY', :countryId,
                 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
            """)
                .setParameter("id", organizationId)
                .setParameter("countryId", country1)
                .executeUpdate();
    }

    private void insertPerson() {

        entityManager.createNativeQuery("""
            INSERT INTO reference.person
                (id, full_name, organization_id, active)
            VALUES
                (:id, 'Dashboard Test Person',
                 :organizationId, true)
            """)
                .setParameter("id", personId)
                .setParameter(
                        "organizationId",
                        organizationId)
                .executeUpdate();
    }

    private void insertQuestionnaires() {

        pwA = findQuestionnaireId("PW_A");
        pwB = findQuestionnaireId("PW_B");
        pwC = findQuestionnaireId("PW_C");
        fg = findQuestionnaireId("F_G");
    }

    private UUID findQuestionnaireId(String code) {

        return (UUID) entityManager.createNativeQuery("""
            SELECT id
            FROM metadata.questionnaire
            WHERE code = :code
            """)
                .setParameter("code", code)
                .getSingleResult();
    }

    private void insertDataCollections() {

        /*
         * Country 1: fully validated + F_G validated.
         */
        insertDataCollection(
                country1, pwA,
                "VALIDATED");

        insertDataCollection(
                country1, pwB,
                "VALIDATED");

        insertDataCollection(
                country1, pwC,
                "VALIDATED");

        insertDataCollection(
                country1, fg,
                "VALIDATED");

        /*
         * Country 2: PW_C not validated,
         * but F_G validated.
         */
        insertDataCollection(
                country2, pwA,
                "VALIDATED");

        insertDataCollection(
                country2, pwB,
                "VALIDATED");

        insertDataCollection(
                country2, pwC,
                "SUBMITTED");

        insertDataCollection(
                country2, fg,
                "VALIDATED");

        /*
         * Country 3: PW incomplete and F_G submitted.
         */
        insertDataCollection(
                country3, pwA,
                "VALIDATED");

        insertDataCollection(
                country3, pwB,
                "SUBMITTED");

        insertDataCollection(
                country3, pwC,
                "SUBMITTED");

        insertDataCollection(
                country3, fg,
                "SUBMITTED");

        /*
         * Country 4: PW incomplete and F_G absent.
         */
        insertDataCollection(
                country4, pwA,
                "SUBMITTED");

        insertDataCollection(
                country4, pwB,
                "SUBMITTED");

        insertDataCollection(
                country4, pwC,
                "SUBMITTED");
    }

    private void insertDataCollection(
            UUID countryId,
            UUID questionnaireId,
            String status) {

        entityManager.createNativeQuery("""
            INSERT INTO reference.data_collection
                (id, campaign_id, country_id,
                 questionnaire_id,
                 responsible_organization_id,
                 data_collector_id,
                 status)
            VALUES
                (:id, :campaignId, :countryId,
                 :questionnaireId,
                 :organizationId,
                 :dataCollectorOrganizationId,
                 :status)
            """)
                .setParameter(
                        "id",
                        UUID.randomUUID())
                .setParameter(
                        "campaignId",
                        campaignId)
                .setParameter(
                        "countryId",
                        countryId)
                .setParameter(
                        "questionnaireId",
                        questionnaireId)
                .setParameter(
                        "organizationId",
                        organizationId)
                .setParameter(
                        "dataCollectorOrganizationId",
                        organizationId)
                .setParameter("status", status)
                .executeUpdate();
    }
}
