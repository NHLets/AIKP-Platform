package org.afdb.aikp.modules.questionnaire.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.junit.jupiter.api.Test;

class QuestionnaireGroupTest {

    @Test
    void shouldCreateRootGroup() {

        QuestionnaireId questionnaireId =
                QuestionnaireId.generate();

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        questionnaireId,
                        null,
                        "  dim_energy  ",
                        "  Energy Dimension  ",
                        "  Energy related indicators  ",
                        QuestionnaireGroupType.DIMENSION,
                        1);

        assertThat(group.getId())
                .isNotNull();

        assertThat(group.getQuestionnaireId())
                .isEqualTo(questionnaireId);

        assertThat(group.getParentGroupId())
                .isNull();

        assertThat(group.getCode())
                .isEqualTo("DIM_ENERGY");

        assertThat(group.getName())
                .isEqualTo("Energy Dimension");

        assertThat(group.getDescription())
                .isEqualTo("Energy related indicators");

        assertThat(group.getGroupType())
                .isEqualTo(QuestionnaireGroupType.DIMENSION);

        assertThat(group.getDisplayOrder())
                .isEqualTo(1);

        assertThat(group.isActive())
                .isTrue();
    }

    @Test
    void shouldCreateChildGroup() {

        QuestionnaireId questionnaireId =
                QuestionnaireId.generate();

        QuestionnaireGroupId parentGroupId =
                QuestionnaireGroupId.generate();

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        questionnaireId,
                        parentGroupId,
                        "policy_group",
                        "Policy Group",
                        null,
                        QuestionnaireGroupType.POLICY_GROUP,
                        2);

        assertThat(group.getQuestionnaireId())
                .isEqualTo(questionnaireId);

        assertThat(group.getParentGroupId())
                .isEqualTo(parentGroupId);

        assertThat(group.getCode())
                .isEqualTo("POLICY_GROUP");

        assertThat(group.getName())
                .isEqualTo("Policy Group");

        assertThat(group.getDescription())
                .isNull();

        assertThat(group.getGroupType())
                .isEqualTo(QuestionnaireGroupType.POLICY_GROUP);

        assertThat(group.getDisplayOrder())
                .isEqualTo(2);

        assertThat(group.isActive())
                .isTrue();
    }

    @Test
    void shouldRestoreExistingGroup() {

        QuestionnaireGroupId groupId =
                QuestionnaireGroupId.generate();

        QuestionnaireId questionnaireId =
                QuestionnaireId.generate();

        QuestionnaireGroupId parentGroupId =
                QuestionnaireGroupId.generate();

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        groupId,
                        questionnaireId,
                        parentGroupId,
                        "POLICY",
                        "Policy",
                        "Description",
                        QuestionnaireGroupType.POLICY_GROUP,
                        3,
                        false);

        assertThat(group.getId())
                .isEqualTo(groupId);

        assertThat(group.getQuestionnaireId())
                .isEqualTo(questionnaireId);

        assertThat(group.getParentGroupId())
                .isEqualTo(parentGroupId);

        assertThat(group.getCode())
                .isEqualTo("POLICY");

        assertThat(group.getName())
                .isEqualTo("Policy");

        assertThat(group.getDescription())
                .isEqualTo("Description");

        assertThat(group.getGroupType())
                .isEqualTo(QuestionnaireGroupType.POLICY_GROUP);

        assertThat(group.getDisplayOrder())
                .isEqualTo(3);

        assertThat(group.isActive())
                .isFalse();
    }

    @Test
    void shouldRejectNullQuestionnaireId() {

        assertThatThrownBy(() ->
                QuestionnaireGroup.create(
                        null,
                        null,
                        "CODE",
                        "Name",
                        null,
                        QuestionnaireGroupType.SECTION,
                        0))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(
                        "Questionnaire id cannot be null.");
    }

    @Test
    void shouldRejectNullGroupType() {

        assertThatThrownBy(() ->
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        null,
                        null,
                        0))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(
                        "Questionnaire group type cannot be null.");
    }

    @Test
    void shouldNormalizeCodeToUpperCaseAndTrimWhitespace() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "  abc_123  ",
                        "Name",
                        null,
                        QuestionnaireGroupType.GROUP,
                        0);

        assertThat(group.getCode())
                .isEqualTo("ABC_123");
    }

    @Test
    void shouldRejectBlankCode() {

        assertThatThrownBy(() ->
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "   ",
                        "Name",
                        null,
                        QuestionnaireGroupType.GROUP,
                        0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Questionnaire group code cannot be null or blank.");
    }

    @Test
    void shouldRejectCodeLongerThan100Characters() {

        String code =
                "A".repeat(101);

        assertThatThrownBy(() ->
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        code,
                        "Name",
                        null,
                        QuestionnaireGroupType.GROUP,
                        0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Questionnaire group code cannot exceed 100 characters.");
    }

    @Test
    void shouldNormalizeNameAndTrimWhitespace() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "  Group Name  ",
                        null,
                        QuestionnaireGroupType.SECTION,
                        0);

        assertThat(group.getName())
                .isEqualTo("Group Name");
    }

    @Test
    void shouldRejectBlankName() {

        assertThatThrownBy(() ->
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "   ",
                        null,
                        QuestionnaireGroupType.SECTION,
                        0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Questionnaire group name cannot be null or blank.");
    }

    @Test
    void shouldRejectNameLongerThan200Characters() {

        String name =
                "A".repeat(201);

        assertThatThrownBy(() ->
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        name,
                        null,
                        QuestionnaireGroupType.SECTION,
                        0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Questionnaire group name cannot exceed 200 characters.");
    }

    @Test
    void shouldNormalizeBlankDescriptionToNull() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        "   ",
                        QuestionnaireGroupType.SECTION,
                        0);

        assertThat(group.getDescription())
                .isNull();
    }

    @Test
    void shouldTrimDescription() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        "  Description  ",
                        QuestionnaireGroupType.SECTION,
                        0);

        assertThat(group.getDescription())
                .isEqualTo("Description");
    }

    @Test
    void shouldRejectNegativeDisplayOrder() {

        assertThatThrownBy(() ->
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        null,
                        QuestionnaireGroupType.SECTION,
                        -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Display order cannot be negative.");
    }

    @Test
    void shouldRenameGroup() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Original",
                        null,
                        QuestionnaireGroupType.SECTION,
                        0);

        group.rename("  New Name  ");

        assertThat(group.getName())
                .isEqualTo("New Name");
    }

    @Test
    void shouldChangeDescription() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        "Original",
                        QuestionnaireGroupType.SECTION,
                        0);

        group.changeDescription("  New description  ");

        assertThat(group.getDescription())
                .isEqualTo("New description");
    }

    @Test
    void shouldChangeGroupType() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        null,
                        QuestionnaireGroupType.SECTION,
                        0);

        group.changeGroupType(
                QuestionnaireGroupType.CATEGORY);

        assertThat(group.getGroupType())
                .isEqualTo(QuestionnaireGroupType.CATEGORY);
    }

    @Test
    void shouldChangeDisplayOrder() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        null,
                        QuestionnaireGroupType.SECTION,
                        1);

        group.changeDisplayOrder(5);

        assertThat(group.getDisplayOrder())
                .isEqualTo(5);
    }

    @Test
    void shouldRejectNegativeDisplayOrderWhenChangingIt() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        null,
                        QuestionnaireGroupType.SECTION,
                        1);

        assertThatThrownBy(() ->
                group.changeDisplayOrder(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Display order cannot be negative.");
    }

    @Test
    void shouldActivateGroup() {

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        QuestionnaireGroupId.generate(),
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        null,
                        QuestionnaireGroupType.SECTION,
                        0,
                        false);

        group.activate();

        assertThat(group.isActive())
                .isTrue();
    }

    @Test
    void shouldDeactivateGroup() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        QuestionnaireId.generate(),
                        null,
                        "CODE",
                        "Name",
                        null,
                        QuestionnaireGroupType.SECTION,
                        0);

        group.deactivate();

        assertThat(group.isActive())
                .isFalse();
    }

}
