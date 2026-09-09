package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireGroupEntity;
import org.junit.jupiter.api.Test;

class QuestionnaireGroupPersistenceMapperTest {

    private final QuestionnaireGroupPersistenceMapper mapper =
            new QuestionnaireGroupPersistenceMapper();

    @Test
    void shouldMapRootGroupToEntity() {

        QuestionnaireId questionnaireId =
                QuestionnaireId.generate();

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        questionnaireId,
                        null,
                        " dim_01 ",
                        "Dimension 1",
                        "Description",
                        QuestionnaireGroupType.DIMENSION,
                        2);

        QuestionnaireGroupEntity entity =
                mapper.toEntity(group);

        assertThat(entity.getId())
                .isEqualTo(group.getId().getValue());

        assertThat(entity.getQuestionnaireId())
                .isEqualTo(questionnaireId.getValue());

        assertThat(entity.getParentGroupId())
                .isNull();

        assertThat(entity.getCode())
                .isEqualTo("DIM_01");

        assertThat(entity.getName())
                .isEqualTo("Dimension 1");

        assertThat(entity.getDescription())
                .isEqualTo("Description");

        assertThat(entity.getGroupType())
                .isEqualTo(QuestionnaireGroupType.DIMENSION);

        assertThat(entity.getDisplayOrder())
                .isEqualTo(2);

        assertThat(entity.isActive())
                .isTrue();
    }

    @Test
    void shouldMapChildGroupToEntity() {

        QuestionnaireId questionnaireId =
                QuestionnaireId.generate();

        QuestionnaireGroupId parentId =
                QuestionnaireGroupId.generate();

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        questionnaireId,
                        parentId,
                        "POLICY_01",
                        "Policy group",
                        null,
                        QuestionnaireGroupType.POLICY_GROUP,
                        3);

        QuestionnaireGroupEntity entity =
                mapper.toEntity(group);

        assertThat(entity.getParentGroupId())
                .isEqualTo(parentId.getValue());

        assertThat(entity.getGroupType())
                .isEqualTo(
                        QuestionnaireGroupType.POLICY_GROUP);

        assertThat(entity.getDescription())
                .isNull();
    }

    @Test
    void shouldMapEntityToRootDomain() {

        UUID id = UUID.randomUUID();

        UUID questionnaireId =
                UUID.randomUUID();

        QuestionnaireGroupEntity entity =
                new QuestionnaireGroupEntity(id);

        entity.setQuestionnaireId(questionnaireId);
        entity.setParentGroupId(null);
        entity.setCode("DIM_01");
        entity.setName("Dimension 1");
        entity.setDescription("Description");
        entity.setGroupType(
                QuestionnaireGroupType.DIMENSION);
        entity.setDisplayOrder(4);
        entity.setActive(true);

        QuestionnaireGroup group =
                mapper.toDomain(entity);

        assertThat(group.getId().getValue())
                .isEqualTo(id);

        assertThat(group.getQuestionnaireId().getValue())
                .isEqualTo(questionnaireId);

        assertThat(group.getParentGroupId())
                .isNull();

        assertThat(group.getCode())
                .isEqualTo("DIM_01");

        assertThat(group.getName())
                .isEqualTo("Dimension 1");

        assertThat(group.getDescription())
                .isEqualTo("Description");

        assertThat(group.getGroupType())
                .isEqualTo(
                        QuestionnaireGroupType.DIMENSION);

        assertThat(group.getDisplayOrder())
                .isEqualTo(4);

        assertThat(group.isActive())
                .isTrue();
    }

    @Test
    void shouldMapEntityToChildDomain() {

        UUID parentId =
                UUID.randomUUID();

        QuestionnaireGroupEntity entity =
                new QuestionnaireGroupEntity(
                        UUID.randomUUID());

        entity.setQuestionnaireId(
                UUID.randomUUID());

        entity.setParentGroupId(parentId);
        entity.setCode("POLICY_01");
        entity.setName("Policy group");
        entity.setGroupType(
                QuestionnaireGroupType.POLICY_GROUP);
        entity.setDisplayOrder(1);
        entity.setActive(false);

        QuestionnaireGroup group =
                mapper.toDomain(entity);

        assertThat(group.getParentGroupId().getValue())
                .isEqualTo(parentId);

        assertThat(group.isActive())
                .isFalse();
    }

    @Test
    void shouldReturnNullWhenMappingNullDomain() {

        assertThat(mapper.toEntity(null))
                .isNull();
    }

    @Test
    void shouldReturnNullWhenMappingNullEntity() {

        assertThat(mapper.toDomain(null))
                .isNull();
    }

    @Test
    void shouldUpdateExistingEntity() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        QuestionnaireGroupId.generate(),
                        "GROUP_01",
                        "Group",
                        "Description",
                        QuestionnaireGroupType.GROUP,
                        5);

        QuestionnaireGroupEntity entity =
                new QuestionnaireGroupEntity(
                        UUID.randomUUID());

        mapper.updateEntity(group, entity);

        assertThat(entity.getQuestionnaireId())
                .isEqualTo(
                        group.getQuestionnaireId().getValue());

        assertThat(entity.getParentGroupId())
                .isEqualTo(
                        group.getParentGroupId().getValue());

        assertThat(entity.getCode())
                .isEqualTo("GROUP_01");

        assertThat(entity.getName())
                .isEqualTo("Group");

        assertThat(entity.getDescription())
                .isEqualTo("Description");

        assertThat(entity.getGroupType())
                .isEqualTo(
                        QuestionnaireGroupType.GROUP);

        assertThat(entity.getDisplayOrder())
                .isEqualTo(5);

        assertThat(entity.isActive())
                .isTrue();
    }

    @Test
    void shouldRejectNullGroupWhenUpdatingEntity() {

        QuestionnaireGroupEntity entity =
                new QuestionnaireGroupEntity(
                        UUID.randomUUID());

        assertThatThrownBy(
                () -> mapper.updateEntity(null, entity))
                .isInstanceOf(
                        IllegalArgumentException.class)
                .hasMessage(
                        "QuestionnaireGroup cannot be null.");
    }

    @Test
    void shouldRejectNullEntityWhenUpdatingEntity() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "GROUP_01",
                        "Group",
                        null,
                        QuestionnaireGroupType.GROUP,
                        0);

        assertThatThrownBy(
                () -> mapper.updateEntity(group, null))
                .isInstanceOf(
                        IllegalArgumentException.class)
                .hasMessage(
                        "QuestionnaireGroupEntity cannot be null.");
    }
}
