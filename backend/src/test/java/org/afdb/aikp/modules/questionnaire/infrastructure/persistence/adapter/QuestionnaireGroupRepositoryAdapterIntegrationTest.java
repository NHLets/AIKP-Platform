package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireGroupRepository;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnaireGroupPersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper.QuestionnairePersistenceMapper;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository.QuestionnaireGroupJpaRepository;

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
        QuestionnaireGroupRepositoryAdapter.class,
        QuestionnaireGroupPersistenceMapper.class,
        QuestionnaireRepositoryAdapter.class,
        QuestionnairePersistenceMapper.class
})
class QuestionnaireGroupRepositoryAdapterIntegrationTest {

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
    private QuestionnaireGroupRepository groupRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private QuestionnaireGroupJpaRepository groupJpaRepository;

    private Questionnaire questionnaire;

    @BeforeEach
    void setUp() {

        questionnaire =
                createAndSaveQuestionnaire();
    }

    @Test
    void shouldSaveAndFindGroupById() {

        QuestionnaireGroup group =
                createGroup(
                        "DIM_01",
                        null,
                        2);

        QuestionnaireGroup saved =
                groupRepository.save(group);

        assertThat(saved.getId())
                .isEqualTo(group.getId());

        assertThat(
                groupRepository.findById(
                        group.getId()))
                .isPresent()
                .get()
                .satisfies(found -> {

                    assertThat(
                            found.getQuestionnaireId())
                            .isEqualTo(
                                    questionnaire.getId());

                    assertThat(
                            found.getParentGroupId())
                            .isNull();

                    assertThat(found.getCode())
                            .isEqualTo("DIM_01");

                    assertThat(found.getGroupType())
                            .isEqualTo(
                                    QuestionnaireGroupType.GROUP);

                    assertThat(found.getDisplayOrder())
                            .isEqualTo(2);
                });
    }

    @Test
    void shouldFindGroupsByQuestionnaireOrderedByDisplayOrder() {

        groupRepository.save(
                createGroup(
                        "GROUP_01",
                        null,
                        2));

        groupRepository.save(
                createGroup(
                        "GROUP_02",
                        null,
                        0));

        groupRepository.save(
                createGroup(
                        "GROUP_03",
                        null,
                        1));

        assertThat(
                groupRepository.findByQuestionnaireId(
                        questionnaire.getId()))
                .extracting(
                        QuestionnaireGroup::getCode)
                .containsExactly(
                        "GROUP_02",
                        "GROUP_03",
                        "GROUP_01");
    }

    @Test
    void shouldFindOnlyRootGroups() {

        QuestionnaireGroup root =
                createGroup(
                        "ROOT_01",
                        null,
                        0);

        groupRepository.save(root);

        groupRepository.save(
                createGroup(
                        "CHILD_01",
                        root.getId(),
                        0));

        assertThat(
                groupRepository.findRootGroupsByQuestionnaireId(
                        questionnaire.getId()))
                .extracting(
                        QuestionnaireGroup::getCode)
                .containsExactly(
                        "ROOT_01");
    }

    @Test
    void shouldFindChildrenOfParentOrderedByDisplayOrder() {

        QuestionnaireGroup parent =
                createGroup(
                        "PARENT",
                        null,
                        0);

        groupRepository.save(parent);

        groupRepository.save(
                createGroup(
                        "CHILD_01",
                        parent.getId(),
                        2));

        groupRepository.save(
                createGroup(
                        "CHILD_02",
                        parent.getId(),
                        0));

        groupRepository.save(
                createGroup(
                        "CHILD_03",
                        parent.getId(),
                        1));

        assertThat(
                groupRepository
                        .findByQuestionnaireIdAndParentGroupId(
                                questionnaire.getId(),
                                parent.getId()))
                .extracting(
                        QuestionnaireGroup::getCode)
                .containsExactly(
                        "CHILD_02",
                        "CHILD_03",
                        "CHILD_01");
    }

    @Test
    void shouldCheckExistingRootGroupCode() {

        groupRepository.save(
                createGroup(
                        "ROOT_CODE",
                        null,
                        0));

        assertThat(
                groupRepository.existsRootGroupCode(
                        questionnaire.getId(),
                        "ROOT_CODE"))
                .isTrue();

        assertThat(
                groupRepository.existsRootGroupCode(
                        questionnaire.getId(),
                        "UNKNOWN"))
                .isFalse();
    }

    @Test
    void shouldCheckExistingChildGroupCode() {

        QuestionnaireGroup parent =
                createGroup(
                        "PARENT_CODE",
                        null,
                        0);

        groupRepository.save(parent);

        groupRepository.save(
                createGroup(
                        "CHILD_CODE",
                        parent.getId(),
                        0));

        assertThat(
                groupRepository.existsChildGroupCode(
                        questionnaire.getId(),
                        parent.getId(),
                        "CHILD_CODE"))
                .isTrue();

        assertThat(
                groupRepository.existsChildGroupCode(
                        questionnaire.getId(),
                        parent.getId(),
                        "UNKNOWN"))
                .isFalse();
    }

    @Test
    void shouldNotFindRootCodeWhenCodeExistsOnlyForChild() {

        QuestionnaireGroup parent =
                createGroup(
                        "PARENT",
                        null,
                        0);

        groupRepository.save(parent);

        groupRepository.save(
                createGroup(
                        "SHARED_CODE",
                        parent.getId(),
                        0));

        assertThat(
                groupRepository.existsRootGroupCode(
                        questionnaire.getId(),
                        "SHARED_CODE"))
                .isFalse();
    }

    @Test
    void shouldNotFindChildCodeUnderAnotherParent() {

        QuestionnaireGroup parent1 =
                createGroup(
                        "PARENT_01",
                        null,
                        0);

        QuestionnaireGroup parent2 =
                createGroup(
                        "PARENT_02",
                        null,
                        1);

        groupRepository.save(parent1);
        groupRepository.save(parent2);

        groupRepository.save(
                createGroup(
                        "CHILD_CODE",
                        parent1.getId(),
                        0));

        assertThat(
                groupRepository.existsChildGroupCode(
                        questionnaire.getId(),
                        parent2.getId(),
                        "CHILD_CODE"))
                .isFalse();
    }

    @Test
    void shouldUpdateExistingGroup() {

        QuestionnaireGroup group =
                createGroup(
                        "GROUP_01",
                        null,
                        0);

        groupRepository.save(group);

        group.rename("Updated Group");
        group.changeDescription(
                "Updated description");
        group.changeDisplayOrder(5);
        group.deactivate();

        QuestionnaireGroup saved =
                groupRepository.save(group);

        assertThat(
                groupRepository.findById(
                        group.getId()))
                .isPresent()
                .get()
                .satisfies(found -> {

                    assertThat(found.getName())
                            .isEqualTo(
                                    "Updated Group");

                    assertThat(found.getDescription())
                            .isEqualTo(
                                    "Updated description");

                    assertThat(found.getDisplayOrder())
                            .isEqualTo(5);

                    assertThat(found.isActive())
                            .isFalse();
                });

        assertThat(saved.isActive())
                .isFalse();
    }

    @Test
    void shouldDeleteGroup() {

        QuestionnaireGroup group =
                createGroup(
                        "DELETE_ME",
                        null,
                        0);

        groupRepository.save(group);

        assertThat(
                groupRepository.findById(
                        group.getId()))
                .isPresent();

        groupRepository.delete(group);

        assertThat(
                groupRepository.findById(
                        group.getId()))
                .isEmpty();
    }

    private QuestionnaireGroup createGroup(
            String code,
            QuestionnaireGroupId parentId,
            int displayOrder) {

        return QuestionnaireGroup.create(
                questionnaire.getId(),
                parentId,
                code,
                "Group " + code,
                "Integration test group",
                QuestionnaireGroupType.GROUP,
                displayOrder);
    }

    private Questionnaire createAndSaveQuestionnaire() {

        String suffix =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();

        Questionnaire questionnaire =
                Questionnaire.create(
                        QuestionnaireCode.of(
                                "QUESTIONNAIRE_"
                                        + suffix),
                        QuestionnaireName.of(
                                "Questionnaire "
                                        + suffix),
                        QuestionnaireDescription.of(
                                "Integration test questionnaire"),
                        QuestionnaireVersion.of(
                                "1.0"),
                        DefaultLanguage.of("en"),
                        RenderType.FORM);

        return questionnaireRepository.save(
                questionnaire);
    }
}
