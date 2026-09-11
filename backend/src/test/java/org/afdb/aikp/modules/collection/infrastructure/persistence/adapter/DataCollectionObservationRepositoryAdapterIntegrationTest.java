package org.afdb.aikp.modules.collection.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.exception.ConstraintViolationException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;
import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.modules.collection.infrastructure.persistence.mapper.DataCollectionObservationPersistenceMapper;
import org.afdb.aikp.modules.collection.infrastructure.persistence.repository.DataCollectionObservationJpaRepository;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
class DataCollectionObservationRepositoryAdapterIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine")
                    .withDatabaseName("aikp")
                    .withUsername("aikp")
                    .withPassword("aikp");

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

        registry.add(
                "spring.jpa.hibernate.ddl-auto",
                () -> "validate");

        registry.add(
                "spring.flyway.enabled",
                () -> "true");

        registry.add(
                "spring.flyway.url",
                postgres::getJdbcUrl);

        registry.add(
                "spring.flyway.user",
                postgres::getUsername);

        registry.add(
                "spring.flyway.password",
                postgres::getPassword);
    }

    @Autowired
    private DataCollectionObservationJpaRepository jpaRepository;

    @Autowired
    private EntityManager entityManager;

    private DataCollectionObservationPersistenceMapper mapper;

    private DataCollectionObservationRepositoryAdapter adapter;

    private TestQuestionnaireVariableRepository variableRepository;

    private Questionnaire questionnaire;

    private QuestionnaireVariable variable;

    private UUID dataCollectionId;

    @BeforeEach
    void setUp() {

        mapper =
                new DataCollectionObservationPersistenceMapper();

        variableRepository =
                new TestQuestionnaireVariableRepository();

        adapter =
                new DataCollectionObservationRepositoryAdapter(
                        jpaRepository,
                        mapper,
                        variableRepository);

        questionnaire =
                createQuestionnaire();

        entityManager.createNativeQuery(
                """
                INSERT INTO metadata.questionnaire
                (
                    id,
                    code,
                    name,
                    description,
                    questionnaire_version,
                    default_language,
                    status,
                    render_type,
                    active,
                    created_at,
                    version
                )
                VALUES
                (
                    :id,
                    :code,
                    :name,
                    :description,
                    :questionnaireVersion,
                    :defaultLanguage,
                    :status,
                    :renderType,
                    :active,
                    CURRENT_TIMESTAMP,
                    0
                )
                """)
                .setParameter(
                        "id",
                        questionnaire.getId().getValue())
                .setParameter(
                        "code",
                        questionnaire.getCode().getValue())
                .setParameter(
                        "name",
                        questionnaire.getName().getValue())
                .setParameter(
                        "description",
                        questionnaire.getDescription()
                                .getValue())
                .setParameter(
                        "questionnaireVersion",
                        questionnaire.getQuestionnaireVersion().getValue())
                .setParameter(
                        "defaultLanguage",
                        questionnaire.getDefaultLanguage()
                                .getValue())
                .setParameter(
                        "status",
                        questionnaire.getStatus().name())
                .setParameter(
                        "renderType",
                        questionnaire.getRenderType().name())
                .setParameter(
                        "active",
                        questionnaire.isActive())
                .executeUpdate();

        variable =
                QuestionnaireVariable.create(
                        questionnaire.getId(),
                        null,
                        "OBS_TEST_" + UUID.randomUUID(),
                        "Test Observation Variable",
                        "Variable used for observation repository integration testing.",
                        QuestionnaireVariableDataType.DECIMAL,
                        "USD",
                        false,
                        1);

        variableRepository.save(variable);

        insertQuestionnaireVariable(variable);

        dataCollectionId =
                UUID.randomUUID();

        insertDataCollection(
                dataCollectionId,
                questionnaire.getId().getValue());

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void shouldSaveAndFindObservationById() {

        DataCollectionObservation observation =
                createObservation(
                        2025,
                        "123.45");

        DataCollectionObservation saved =
                adapter.save(observation);

        entityManager.flush();
        entityManager.clear();

        assertThat(saved.getId())
                .isEqualTo(observation.getId());

        Optional<DataCollectionObservation> found =
                adapter.findById(observation.getId());

        assertThat(found)
                .isPresent();

        assertThat(found.get().getDataCollectionId()
                .getValue())
                .isEqualTo(dataCollectionId);

        assertThat(found.get().getQuestionnaireVariableId())
                .isEqualTo(variable.getId());

        assertThat(found.get().getReferenceYear())
                .isEqualTo(2025);

        assertThat(found.get().getStatus())
                .isEqualTo(ObservationStatus.PROVIDED);

        assertThat(found.get().getNumericValue())
                .isEqualByComparingTo("123.45");
    }

    @Test
    void shouldFindObservationsByDataCollectionIdInYearOrder() {

        DataCollectionId collectionId =
                DataCollectionId.of(dataCollectionId);

        adapter.save(
                createObservation(
                        collectionId,
                        2025,
                        "200"));

        adapter.save(
                createObservation(
                        collectionId,
                        2015,
                        "100"));

        adapter.save(
                createObservation(
                        collectionId,
                        2020,
                        "150"));

        entityManager.flush();
        entityManager.clear();

        List<DataCollectionObservation> observations =
                adapter.findByDataCollectionId(
                        collectionId);

        assertThat(observations)
                .hasSize(3);

        assertThat(observations)
                .extracting(
                        DataCollectionObservation::getReferenceYear)
                .containsExactly(
                        2015,
                        2020,
                        2025);
    }

    @Test
    void shouldFindObservationsByQuestionnaireVariableId() {

        adapter.save(
                createObservation(
                        2020,
                        "150"));

        entityManager.flush();
        entityManager.clear();

        List<DataCollectionObservation> observations =
                adapter.findByQuestionnaireVariableId(
                        variable.getId());

        assertThat(observations)
                .hasSize(1);

        assertThat(
                observations.get(0).getReferenceYear())
                .isEqualTo(2020);
    }

    @Test
    void shouldFindObservationByCollectionVariableAndYear() {

        DataCollectionObservation observation =
                createObservation(
                        2022,
                        "175.50");

        adapter.save(observation);

        entityManager.flush();
        entityManager.clear();

        Optional<DataCollectionObservation> found =
                adapter
                        .findByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                                DataCollectionId.of(
                                        dataCollectionId),
                                variable.getId(),
                                2022);

        assertThat(found)
                .isPresent();

        assertThat(found.get().getReferenceYear())
                .isEqualTo(2022);

        assertThat(found.get().getNumericValue())
                .isEqualByComparingTo("175.50");
    }

    @Test
    void shouldCheckObservationExistence() {

        DataCollectionId collectionId =
                DataCollectionId.of(dataCollectionId);

        assertThat(
                adapter
                        .existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                                collectionId,
                                variable.getId(),
                                2019))
                .isFalse();

        adapter.save(
                createObservation(
                        collectionId,
                        2019,
                        "99.99"));

        entityManager.flush();
        entityManager.clear();

        assertThat(
                adapter
                        .existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                                collectionId,
                                variable.getId(),
                                2019))
                .isTrue();
    }

    @Test
    void shouldRejectDuplicateObservationForSameCollectionVariableAndYear() {

        DataCollectionObservation first =
                createObservation(
                        2024,
                        "100.00");

        DataCollectionObservation duplicate =
                createObservation(
                        2024,
                        "200.00");

        adapter.save(first);
        entityManager.flush();
        entityManager.clear();

        adapter.save(duplicate);

        org.junit.jupiter.api.Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> entityManager.flush());
    }

    @Test
    void shouldDeleteObservation() {

        DataCollectionObservation observation =
                createObservation(
                        2021,
                        "111.11");

        adapter.save(observation);

        entityManager.flush();
        entityManager.clear();

        assertThat(
                adapter.findById(
                        observation.getId()))
                .isPresent();

        adapter.delete(observation);

        entityManager.flush();
        entityManager.clear();

        assertThat(
                adapter.findById(
                        observation.getId()))
                .isEmpty();
    }

    private DataCollectionObservation createObservation(
            int year,
            String value) {

        return createObservation(
                DataCollectionId.of(dataCollectionId),
                year,
                value);
    }

    private DataCollectionObservation createObservation(
            DataCollectionId collectionId,
            int year,
            String value) {

        return DataCollectionObservation.provided(
                DataCollectionObservationId.generate(),
                collectionId,
                variable.getId(),
                year,
                QuestionnaireVariableDataType.DECIMAL,
                new BigDecimal(value),
                null,
                null,
                null,
                "USD",
                null);
    }

    private Questionnaire createQuestionnaire() {

        return Questionnaire.create(
                QuestionnaireCode.of(
                        "OBS_IT_" + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .toUpperCase()),
                QuestionnaireName.of(
                        "Observation Integration Questionnaire"),
                QuestionnaireDescription.of(
                        "Questionnaire used for observation integration testing."),
                QuestionnaireVersion.of("1.0"),
                DefaultLanguage.of("en"),
                RenderType.FORM);
    }

    private void insertQuestionnaireVariable(
            QuestionnaireVariable variable) {

        entityManager.createNativeQuery(
                """
                INSERT INTO metadata.questionnaire_variable
                (
                    id,
                    questionnaire_id,
                    questionnaire_group_id,
                    series_code,
                    name,
                    definition,
                    data_type,
                    unit,
                    required,
                    display_order,
                    active,
                    created_at,
                    version
                )
                VALUES
                (
                    :id,
                    :questionnaireId,
                    NULL,
                    :seriesCode,
                    :name,
                    :definition,
                    :dataType,
                    :unit,
                    :required,
                    :displayOrder,
                    true,
                    CURRENT_TIMESTAMP,
                    0
                )
                """)
                .setParameter(
                        "id",
                        variable.getId().getValue())
                .setParameter(
                        "questionnaireId",
                        variable.getQuestionnaireId()
                                .getValue())
                .setParameter(
                        "seriesCode",
                        variable.getSeriesCode())
                .setParameter(
                        "name",
                        variable.getName())
                .setParameter(
                        "definition",
                        variable.getDefinition())
                .setParameter(
                        "dataType",
                        variable.getDataType().name())
                .setParameter(
                        "unit",
                        variable.getUnit())
                .setParameter(
                        "required",
                        variable.isRequired())
                .setParameter(
                        "displayOrder",
                        variable.getDisplayOrder())
                .executeUpdate();
    }

    private void insertDataCollection(
            UUID collectionId,
            UUID questionnaireId) {

        UUID countryId =
                UUID.randomUUID();

        UUID campaignId =
                UUID.randomUUID();

        UUID organizationId =
                UUID.randomUUID();

        UUID personId =
                UUID.randomUUID();

        entityManager.createNativeQuery(
                """
                INSERT INTO reference.country
                (
                    id,
                    iso2_code,
                    iso3_code,
                    numeric_code,
                    name,
                    official_name,
                    active,
                    created_at,
                    version
                )
                VALUES
                (
                    :id,
                    :iso2,
                    :iso3,
                    :numeric,
                    :name,
                    :officialName,
                    true,
                    CURRENT_TIMESTAMP,
                    0
                )
                """)
                .setParameter(
                        "id",
                        countryId)
                .setParameter(
                        "iso2",
                        "T" + UUID.randomUUID()
                                .toString()
                                .substring(0, 1)
                                .toUpperCase())
                .setParameter(
                        "iso3",
                        UUID.randomUUID()
                                .toString()
                                .replace("-", "")
                                .substring(0, 3)
                                .toUpperCase())
                .setParameter(
                        "numeric",
                        "9" + String.format(
                                "%02d",
                                Math.abs(
                                        UUID.randomUUID()
                                                .hashCode())
                                        % 100))
                .setParameter(
                        "name",
                        "Observation Test Country")
                .setParameter(
                        "officialName",
                        "Observation Test Country Official")
                .executeUpdate();

        entityManager.createNativeQuery(
                """
                INSERT INTO campaign.campaign
                (
                    id,
                    code,
                    name,
                    description,
                    start_date,
                    end_date,
                    status,
                    created_at,
                    version
                )
                VALUES
                (
                    :id,
                    :code,
                    :name,
                    :description,
                    DATE '2026-01-01',
                    DATE '2026-12-31',
                    'DRAFT',
                    CURRENT_TIMESTAMP,
                    0
                )
                """)
                .setParameter(
                        "id",
                        campaignId)
                .setParameter(
                        "code",
                        "OBS_CAMP_" + UUID.randomUUID())
                .setParameter(
                        "name",
                        "Observation Integration Campaign")
                .setParameter(
                        "description",
                        "Campaign used for observation integration testing.")
                .executeUpdate();

        entityManager.createNativeQuery(
                """
                INSERT INTO reference.organization
                (
                    id,
                    code,
                    name,
                    type,
                    country_id,
                    active,
                    created_at,
                    version
                )
                VALUES
                (
                    :id,
                    :code,
                    :name,
                    :type,
                    :countryId,
                    true,
                    CURRENT_TIMESTAMP,
                    0
                )
                """)
                .setParameter(
                        "id",
                        organizationId)
                .setParameter(
                        "code",
                        "OBS_ORG_" + UUID.randomUUID())
                .setParameter(
                        "name",
                        "Observation Test Organization")
                .setParameter(
                        "type",
                        "OTHER")
                .setParameter(
                        "countryId",
                        countryId)
                .executeUpdate();

        entityManager.createNativeQuery(
                """
                INSERT INTO reference.person
                (
                    id,
                    full_name,
                    organization_id,
                    active
                )
                VALUES
                (
                    :id,
                    :fullName,
                    :organizationId,
                    true
                )
                """)
                .setParameter(
                        "id",
                        personId)
                .setParameter(
                        "fullName",
                        "Observation Integration Tester")
                .setParameter(
                        "organizationId",
                        organizationId)
                .executeUpdate();

        entityManager.createNativeQuery(
                """
                INSERT INTO reference.data_collection
                (
                    id,
                    campaign_id,
                    country_id,
                    questionnaire_id,
                    responsible_organization_id,
                    data_collector_id,
                    status
                )
                VALUES
                (
                    :id,
                    :campaignId,
                    :countryId,
                    :questionnaireId,
                    :organizationId,
                    :personId,
                    :status
                )
                """)
                .setParameter(
                        "id",
                        collectionId)
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
                        "personId",
                        personId)
                .setParameter(
                        "status",
                        DataCollectionStatus.DRAFT.name())
                .executeUpdate();
    }

    private static class TestQuestionnaireVariableRepository
            implements QuestionnaireVariableRepository {

        private final Map<
                QuestionnaireVariableId,
                QuestionnaireVariable> variables =
                new HashMap<>();

        @Override
        public QuestionnaireVariable save(
                QuestionnaireVariable variable) {

            variables.put(
                    variable.getId(),
                    variable);

            return variable;
        }

        @Override
        public Optional<QuestionnaireVariable> findById(
                QuestionnaireVariableId id) {

            return Optional.ofNullable(
                    variables.get(id));
        }

        @Override
        public List<QuestionnaireVariable>
                findByQuestionnaireId(
                        QuestionnaireId questionnaireId) {

            return variables.values()
                    .stream()
                    .filter(variable ->
                            variable.getQuestionnaireId()
                                    .equals(questionnaireId))
                    .toList();
        }

        @Override
        public List<QuestionnaireVariable>
                findByQuestionnaireGroupId(
                        QuestionnaireGroupId questionnaireGroupId) {

            return variables.values()
                    .stream()
                    .filter(variable ->
                            questionnaireGroupId.equals(
                                    variable.getQuestionnaireGroupId()))
                    .toList();
        }

        @Override
        public boolean existsByQuestionnaireIdAndSeriesCode(
                QuestionnaireId questionnaireId,
                String seriesCode) {

            return variables.values()
                    .stream()
                    .anyMatch(variable ->
                            variable.getQuestionnaireId()
                                    .equals(questionnaireId)
                                    && variable.getSeriesCode()
                                            .equals(seriesCode));
        }

        @Override
        public void delete(
                QuestionnaireVariable variable) {

            variables.remove(variable.getId());
        }
    }
}
