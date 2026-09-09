package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireGroupRepository;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnaireGroupPersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnairePersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnaireVariablePersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository.QuestionnaireVariableJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        QuestionnaireVariableRepositoryAdapter.class,
        QuestionnaireVariablePersistenceMapper.class,
        QuestionnaireGroupRepositoryAdapter.class,
        QuestionnaireGroupPersistenceMapper.class,
        QuestionnaireRepositoryAdapter.class,
        QuestionnairePersistenceMapper.class
})
class QuestionnaireVariableRepositoryAdapterIntegrationTest {

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
    private QuestionnaireVariableRepository variableRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private QuestionnaireGroupRepository groupRepository;

    @Autowired
    private QuestionnaireVariableJpaRepository variableJpaRepository;

    private Questionnaire questionnaire;

    @BeforeEach
    void setUp() {
        questionnaire = createAndSaveQuestionnaire();
    }

    @Test
    void shouldSaveAndFindVariableById() {

        QuestionnaireVariable variable =
                createVariable(
                        "GDP_001",
                        null,
                        2);

        QuestionnaireVariable saved =
                variableRepository.save(variable);

        assertThat(saved.getId())
                .isEqualTo(variable.getId());

        assertThat(
                variableRepository.findById(
                        variable.getId()))
                .isPresent()
                .get()
                .satisfies(found -> {

                    assertThat(found.getQuestionnaireId())
                            .isEqualTo(questionnaire.getId());

                    assertThat(found.getQuestionnaireGroupId())
                            .isNull();

                    assertThat(found.getSeriesCode())
                            .isEqualTo("GDP_001");

                    assertThat(found.getName())
                            .isEqualTo("Variable GDP_001");

                    assertThat(found.getDataType())
                            .isEqualTo(
                                    QuestionnaireVariableDataType.NUMBER);

                    assertThat(found.getUnit())
                            .isEqualTo("USD");

                    assertThat(found.isRequired())
                            .isTrue();

                    assertThat(found.getDisplayOrder())
                            .isEqualTo(2);

                    assertThat(found.isActive())
                            .isTrue();
                });
    }

    @Test
    void shouldFindVariablesByQuestionnaireOrderedByDisplayOrder() {

        variableRepository.save(
                createVariable(
                        "VAR_01",
                        null,
                        2));

        variableRepository.save(
                createVariable(
                        "VAR_02",
                        null,
                        0));

        variableRepository.save(
                createVariable(
                        "VAR_03",
                        null,
                        1));

        assertThat(
                variableRepository.findByQuestionnaireId(
                        questionnaire.getId()))
                .extracting(
                        QuestionnaireVariable::getSeriesCode)
                .containsExactly(
                        "VAR_02",
                        "VAR_03",
                        "VAR_01");
    }

    @Test
    void shouldFindVariablesByQuestionnaireGroupId() {

        QuestionnaireGroup group =
                createAndSaveGroup();

        QuestionnaireGroupId groupId =
                group.getId();

        variableRepository.save(
                createVariable(
                        "GROUP_VAR_01",
                        groupId,
                        1));

        variableRepository.save(
                createVariable(
                        "GROUP_VAR_02",
                        groupId,
                        0));

        assertThat(
                variableRepository.findByQuestionnaireGroupId(
                        groupId))
                .extracting(
                        QuestionnaireVariable::getSeriesCode)
                .containsExactly(
                        "GROUP_VAR_02",
                        "GROUP_VAR_01");
    }

    @Test
    void shouldReturnTrueWhenSeriesCodeExists() {

        variableRepository.save(
                createVariable(
                        "EXISTS_01",
                        null,
                        0));

        assertThat(
                variableRepository
                        .existsByQuestionnaireIdAndSeriesCode(
                                questionnaire.getId(),
                                "EXISTS_01"))
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenSeriesCodeDoesNotExist() {

        assertThat(
                variableRepository
                        .existsByQuestionnaireIdAndSeriesCode(
                                questionnaire.getId(),
                                "NOT_EXISTS"))
                .isFalse();
    }

    @Test
    void shouldDeleteVariable() {

        QuestionnaireVariable variable =
                createVariable(
                        "DELETE_ME",
                        null,
                        0);

        variableRepository.save(variable);

        assertThat(
                variableRepository.findById(
                        variable.getId()))
                .isPresent();

        variableRepository.delete(variable);

        assertThat(
                variableRepository.findById(
                        variable.getId()))
                .isEmpty();
    }

    @Test
    void shouldSaveVariableWithGroup() {

        QuestionnaireGroup group =
                createAndSaveGroup();

        QuestionnaireGroupId groupId =
                group.getId();

        QuestionnaireVariable variable =
                createVariable(
                        "GROUPED_01",
                        groupId,
                        0);

        variableRepository.save(variable);

        assertThat(
                variableRepository.findById(
                        variable.getId()))
                .isPresent()
                .get()
                .extracting(
                        QuestionnaireVariable::getQuestionnaireGroupId)
                .isEqualTo(groupId);
    }

    @Test
    void shouldUpdateExistingVariable() {

        QuestionnaireVariable variable =
                createVariable(
                        "UPDATE_01",
                        null,
                        0);

        variableRepository.save(variable);

        variable.rename("Updated Variable");
        variable.changeDefinition("Updated definition");
        variable.changeUnit("EUR");
        variable.changeDisplayOrder(5);
        variable.markAsOptional();
        variable.deactivate();

        variableRepository.save(variable);

        assertThat(
                variableRepository.findById(
                        variable.getId()))
                .isPresent()
                .get()
                .satisfies(found -> {

                    assertThat(found.getName())
                            .isEqualTo("Updated Variable");

                    assertThat(found.getDefinition())
                            .isEqualTo("Updated definition");

                    assertThat(found.getUnit())
                            .isEqualTo("EUR");

                    assertThat(found.getDisplayOrder())
                            .isEqualTo(5);

                    assertThat(found.isRequired())
                            .isFalse();

                    assertThat(found.isActive())
                            .isFalse();
                });
    }

    @Test
    void shouldSaveOptionalVariable() {

        QuestionnaireVariable variable =
                QuestionnaireVariable.create(
                        questionnaire.getId(),
                        null,
                        "OPTIONAL_01",
                        "Optional variable",
                        null,
                        QuestionnaireVariableDataType.TEXT,
                        null,
                        false,
                        0);

        QuestionnaireVariable saved =
                variableRepository.save(variable);

        assertThat(saved.isRequired())
                .isFalse();

        assertThat(saved.getUnit())
                .isNull();

        assertThat(saved.getDefinition())
                .isNull();
    }

    @Test
    void shouldPreserveVariableDataType() {

        QuestionnaireVariable variable =
                QuestionnaireVariable.create(
                        questionnaire.getId(),
                        null,
                        "PERCENT_01",
                        "Percentage variable",
                        "Percentage value",
                        QuestionnaireVariableDataType.PERCENTAGE,
                        "%",
                        true,
                        0);

        variableRepository.save(variable);

        assertThat(
                variableRepository.findById(
                        variable.getId()))
                .isPresent()
                .get()
                .satisfies(found -> {

                    assertThat(found.getDataType())
                            .isEqualTo(
                                    QuestionnaireVariableDataType.PERCENTAGE);

                    assertThat(found.getUnit())
                            .isEqualTo("%");
                });
    }

    private org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup
            createAndSaveGroup() {

        org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup group =
                org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup.create(
                        questionnaire.getId(),
                        null,
                        "GROUP_" + UUID.randomUUID()
                                .toString()
                                .substring(0, 8),
                        "Integration Test Group",
                        "Integration test group",
                        org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType.GROUP,
                        0);

        return groupRepository.save(group);
    }

    private QuestionnaireVariable createVariable(
            String seriesCode,
            QuestionnaireGroupId groupId,
            int displayOrder) {

        return QuestionnaireVariable.create(
                questionnaire.getId(),
                groupId,
                seriesCode,
                "Variable " + seriesCode,
                "Integration test variable",
                QuestionnaireVariableDataType.NUMBER,
                "USD",
                true,
                displayOrder);
    }

    private Questionnaire createAndSaveQuestionnaire() {

        Questionnaire questionnaire =
                Questionnaire.create(
                        QuestionnaireCode.of(
                                "TEST_" + UUID.randomUUID()
                                        .toString()
                                        .substring(0, 8)),
                        QuestionnaireName.of(
                                "Integration Test Questionnaire"),
                        QuestionnaireDescription.of(
                                "Integration test questionnaire"),
                        QuestionnaireVersion.of("1"),
                        DefaultLanguage.of("en"),
                        RenderType.FORM);

        return questionnaireRepository.save(questionnaire);
    }
}
