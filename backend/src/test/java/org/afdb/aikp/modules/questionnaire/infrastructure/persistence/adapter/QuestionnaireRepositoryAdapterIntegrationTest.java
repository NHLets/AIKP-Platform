package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.adapter;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireEntity;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnairePersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository.QuestionnaireJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
class QuestionnaireRepositoryAdapterIntegrationTest {

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
    private QuestionnaireJpaRepository jpaRepository;

    @Autowired
    private EntityManager entityManager;

    private QuestionnairePersistenceMapper mapper;

    private QuestionnaireRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {

        mapper = new QuestionnairePersistenceMapper();

        adapter = new QuestionnaireRepositoryAdapter(
                jpaRepository,
                mapper);
    }


    @Test
    void shouldSaveAndFindQuestionnaireById() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_IT_001");

        Questionnaire saved =
                adapter.save(questionnaire);

        assertThat(saved.getId())
                .isEqualTo(questionnaire.getId());

        Optional<Questionnaire> found =
                adapter.findById(questionnaire.getId());

        assertThat(found)
                .isPresent();

        assertThat(found.get().getCode().getValue())
                .isEqualTo("PW_IT_001");

        assertThat(found.get().getName().getValue())
                .isEqualTo("Questionnaire PW_IT_001");

        assertThat(found.get().getStatus())
                .isEqualTo(QuestionnaireStatus.DRAFT);

        assertThat(found.get().getRenderType())
                .isEqualTo(RenderType.FORM);

        assertThat(found.get().isActive())
                .isTrue();
    }

    @Test
    void shouldFindQuestionnaireByCode() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_IT_002");

        adapter.save(questionnaire);

        Optional<Questionnaire> found =
                adapter.findByCode(
                        QuestionnaireCode.of("PW_IT_002"));

        assertThat(found)
                .isPresent();

        assertThat(found.get().getId())
                .isEqualTo(questionnaire.getId());
    }

    @Test
    void shouldFindAllQuestionnaires() {

        adapter.save(
                createQuestionnaire("PW_IT_003"));

        adapter.save(
                createQuestionnaire("PW_IT_004"));

        List<Questionnaire> questionnaires =
                adapter.findAll();

        assertThat(questionnaires)
                .hasSize(2);

        assertThat(questionnaires)
                .extracting(q ->
                        q.getCode().getValue())
                .containsExactlyInAnyOrder(
                        "PW_IT_003",
                        "PW_IT_004");
    }

    @Test
    void shouldFindActiveQuestionnaires() {

        Questionnaire active =
                createQuestionnaire("PW_IT_005");

        Questionnaire inactive =
                createQuestionnaire("PW_IT_006");

        inactive.deactivate();

        adapter.save(active);
        adapter.save(inactive);

        List<Questionnaire> result =
                adapter.findActive();

        assertThat(result)
                .extracting(q ->
                        q.getCode().getValue())
                .contains("PW_IT_005")
                .doesNotContain("PW_IT_006");
    }

    @Test
    void shouldFindQuestionnairesByStatus() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_IT_007");

        questionnaire.submitForReview();
        questionnaire.approve();

        adapter.save(questionnaire);

        List<Questionnaire> result =
                adapter.findByStatus(
                        QuestionnaireStatus.APPROVED);

        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0).getCode().getValue())
                .isEqualTo("PW_IT_007");
    }

    @Test
    void shouldCheckExistenceById() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_IT_008");

        adapter.save(questionnaire);

        assertThat(
                adapter.existsById(
                        questionnaire.getId()))
                .isTrue();

        assertThat(
                adapter.existsById(
                        QuestionnaireId.generate()))
                .isFalse();
    }

    @Test
    void shouldCheckExistenceByCode() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_IT_009");

        adapter.save(questionnaire);

        assertThat(
                adapter.existsByCode(
                        QuestionnaireCode.of("PW_IT_009")))
                .isTrue();

        assertThat(
                adapter.existsByCode(
                        QuestionnaireCode.of("DOES_NOT_EXIST")))
                .isFalse();
    }

    @Test
    void shouldDeleteQuestionnaire() {

    Questionnaire questionnaire =
            createQuestionnaire("PW_IT_010");

    adapter.save(questionnaire);

    entityManager.flush();
    entityManager.clear();

    assertThat(
            adapter.findById(
                    questionnaire.getId()))
            .isPresent();

    adapter.delete(questionnaire);

    entityManager.flush();
    entityManager.clear();

    assertThat(
            adapter.findById(
                    questionnaire.getId()))
            .isEmpty();
        }

    @Test
    void shouldPersistLifecycleState() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_IT_011");

        questionnaire.submitForReview();
        questionnaire.approve();
        questionnaire.publish();

        adapter.save(questionnaire);

        Optional<Questionnaire> found =
                adapter.findById(
                        questionnaire.getId());

        assertThat(found)
                .isPresent();

        assertThat(found.get().getStatus())
                .isEqualTo(
                        QuestionnaireStatus.PUBLISHED);
    }

    private Questionnaire createQuestionnaire(
            String code) {

        return Questionnaire.create(
                QuestionnaireCode.of(code),
                QuestionnaireName.of(
                        "Questionnaire " + code),
                QuestionnaireDescription.of(
                        "Integration test questionnaire"),
                QuestionnaireVersion.of("1.0"),
                DefaultLanguage.of("en"),
                RenderType.FORM);
    }
}