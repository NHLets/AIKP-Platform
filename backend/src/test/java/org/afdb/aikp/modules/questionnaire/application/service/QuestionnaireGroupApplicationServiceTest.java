package org.afdb.aikp.modules.questionnaire.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.application.dto.CreateQuestionnaireGroupRequest;
import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireGroupResponse;
import org.afdb.aikp.modules.questionnaire.application.dto.UpdateQuestionnaireGroupRequest;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireGroupRepository;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuestionnaireGroupApplicationServiceTest {

    @Mock
    private QuestionnaireGroupRepository groupRepository;

    @Mock
    private QuestionnaireVariableRepository variableRepository;

    @InjectMocks
    private QuestionnaireGroupApplicationService service;

    private QuestionnaireId questionnaireId;
    private QuestionnaireGroupId groupId;

    @BeforeEach
    void setUp() {
        questionnaireId = QuestionnaireId.generate();
        groupId = QuestionnaireGroupId.generate();
    }

    @Test
    void shouldCreateRootGroup() {

        CreateQuestionnaireGroupRequest request =
                new CreateQuestionnaireGroupRequest(
                        questionnaireId.getValue(),
                        null,
                        "ENERGY",
                        "Energy",
                        "Energy indicators",
                        QuestionnaireGroupType.DIMENSION,
                        1);

        QuestionnaireGroup saved =
                QuestionnaireGroup.create(
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        "Energy indicators",
                        QuestionnaireGroupType.DIMENSION,
                        1);

        when(groupRepository.existsRootGroupCode(
                questionnaireId,
                "ENERGY"))
                .thenReturn(false);

        when(groupRepository.save(any(QuestionnaireGroup.class)))
                .thenReturn(saved);

        QuestionnaireGroupResponse response =
                service.createGroup(request);

        assertThat(response)
                .isNotNull();

        assertThat(response.questionnaireId())
                .isEqualTo(questionnaireId.getValue());

        assertThat(response.code())
                .isEqualTo("ENERGY");

        assertThat(response.name())
                .isEqualTo("Energy");

        verify(groupRepository)
                .existsRootGroupCode(
                        questionnaireId,
                        "ENERGY");

        verify(groupRepository)
                .save(any(QuestionnaireGroup.class));
    }

    @Test
    void shouldCreateChildGroupWhenParentExistsInSameQuestionnaire() {

        QuestionnaireGroupId parentId =
                QuestionnaireGroupId.generate();

        QuestionnaireGroup parent =
                QuestionnaireGroup.restore(
                        parentId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        CreateQuestionnaireGroupRequest request =
                new CreateQuestionnaireGroupRequest(
                        questionnaireId.getValue(),
                        parentId.getValue(),
                        "POWER",
                        "Power",
                        null,
                        QuestionnaireGroupType.POLICY_GROUP,
                        2);

        QuestionnaireGroup saved =
                QuestionnaireGroup.create(
                        questionnaireId,
                        parentId,
                        "POWER",
                        "Power",
                        null,
                        QuestionnaireGroupType.POLICY_GROUP,
                        2);

        when(groupRepository.findById(parentId))
                .thenReturn(Optional.of(parent));

        when(groupRepository.existsChildGroupCode(
                questionnaireId,
                parentId,
                "POWER"))
                .thenReturn(false);

        when(groupRepository.save(any(QuestionnaireGroup.class)))
                .thenReturn(saved);

        QuestionnaireGroupResponse response =
                service.createGroup(request);

        assertThat(response.parentGroupId())
                .isEqualTo(parentId.getValue());

        assertThat(response.code())
                .isEqualTo("POWER");

        verify(groupRepository)
                .findById(parentId);

        verify(groupRepository)
                .existsChildGroupCode(
                        questionnaireId,
                        parentId,
                        "POWER");
    }

    @Test
    void shouldRejectDimensionWithParent() {

        QuestionnaireGroupId parentId =
                QuestionnaireGroupId.generate();

        CreateQuestionnaireGroupRequest request =
                new CreateQuestionnaireGroupRequest(
                        questionnaireId.getValue(),
                        parentId.getValue(),
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1);

        assertThatThrownBy(() ->
                service.createGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "A DIMENSION cannot have a parent group.");

        verify(groupRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectMissingParentGroup() {

        QuestionnaireGroupId parentId =
                QuestionnaireGroupId.generate();

        CreateQuestionnaireGroupRequest request =
                new CreateQuestionnaireGroupRequest(
                        questionnaireId.getValue(),
                        parentId.getValue(),
                        "POWER",
                        "Power",
                        null,
                        QuestionnaireGroupType.POLICY_GROUP,
                        1);

        when(groupRepository.findById(parentId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.createGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Parent questionnaire group not found:");

        verify(groupRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectParentFromAnotherQuestionnaire() {

        QuestionnaireGroupId parentId =
                QuestionnaireGroupId.generate();

        QuestionnaireGroup parent =
                QuestionnaireGroup.restore(
                        parentId,
                        QuestionnaireId.generate(),
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        CreateQuestionnaireGroupRequest request =
                new CreateQuestionnaireGroupRequest(
                        questionnaireId.getValue(),
                        parentId.getValue(),
                        "POWER",
                        "Power",
                        null,
                        QuestionnaireGroupType.POLICY_GROUP,
                        1);

        when(groupRepository.findById(parentId))
                .thenReturn(Optional.of(parent));

        assertThatThrownBy(() ->
                service.createGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Parent group must belong to the same questionnaire.");

        verify(groupRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectDuplicateRootGroupCode() {

        CreateQuestionnaireGroupRequest request =
                new CreateQuestionnaireGroupRequest(
                        questionnaireId.getValue(),
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1);

        when(groupRepository.existsRootGroupCode(
                questionnaireId,
                "ENERGY"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.createGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "A questionnaire group with code 'ENERGY' already exists in this location.");

        verify(groupRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectDuplicateChildGroupCode() {

        QuestionnaireGroupId parentId =
                QuestionnaireGroupId.generate();

        QuestionnaireGroup parent =
                QuestionnaireGroup.restore(
                        parentId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        CreateQuestionnaireGroupRequest request =
                new CreateQuestionnaireGroupRequest(
                        questionnaireId.getValue(),
                        parentId.getValue(),
                        "POWER",
                        "Power",
                        null,
                        QuestionnaireGroupType.POLICY_GROUP,
                        1);

        when(groupRepository.findById(parentId))
                .thenReturn(Optional.of(parent));

        when(groupRepository.existsChildGroupCode(
                questionnaireId,
                parentId,
                "POWER"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.createGroup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "A questionnaire group with code 'POWER' already exists in this location.");

        verify(groupRepository, never())
                .save(any());
    }

    @Test
    void shouldGetGroupById() {

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        groupId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        "Description",
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        QuestionnaireGroupResponse response =
                service.getGroupById(
                        groupId.getValue());

        assertThat(response.id())
                .isEqualTo(groupId.getValue());

        assertThat(response.code())
                .isEqualTo("ENERGY");

        assertThat(response.name())
                .isEqualTo("Energy");
    }

    @Test
    void shouldRejectGetGroupByIdWhenNotFound() {

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.getGroupById(groupId.getValue()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Questionnaire group not found:");
    }

    @Test
    void shouldGetGroupsByQuestionnaire() {

        QuestionnaireGroup first =
                QuestionnaireGroup.restore(
                        QuestionnaireGroupId.generate(),
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        QuestionnaireGroup second =
                QuestionnaireGroup.restore(
                        QuestionnaireGroupId.generate(),
                        questionnaireId,
                        null,
                        "TRANSPORT",
                        "Transport",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        2,
                        true);

        when(groupRepository.findByQuestionnaireId(
                questionnaireId))
                .thenReturn(List.of(first, second));

        List<QuestionnaireGroupResponse> responses =
                service.getGroupsByQuestionnaire(
                        questionnaireId.getValue());

        assertThat(responses)
                .hasSize(2);

        assertThat(responses.get(0).code())
                .isEqualTo("ENERGY");

        assertThat(responses.get(1).code())
                .isEqualTo("TRANSPORT");
    }

    @Test
    void shouldUpdateGroup() {

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        groupId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        "Original",
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        UpdateQuestionnaireGroupRequest request =
                new UpdateQuestionnaireGroupRequest(
                        "Updated Energy",
                        "Updated description",
                        4);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        when(groupRepository.save(any(QuestionnaireGroup.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        QuestionnaireGroupResponse response =
                service.updateGroup(
                        groupId.getValue(),
                        request);

        assertThat(response.name())
                .isEqualTo("Updated Energy");

        assertThat(response.description())
                .isEqualTo("Updated description");

        assertThat(response.displayOrder())
                .isEqualTo(4);

        verify(groupRepository)
                .save(group);
    }

    @Test
    void shouldRejectUpdateWhenNameIsBlank() {

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        groupId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        "Original",
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        UpdateQuestionnaireGroupRequest request =
                new UpdateQuestionnaireGroupRequest(
                        "   ",
                        "Updated description",
                        4);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        assertThatThrownBy(() ->
                service.updateGroup(
                        groupId.getValue(),
                        request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Questionnaire group name cannot be null or blank.");

        verify(groupRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectUpdateWhenDisplayOrderIsNegative() {

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        groupId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        "Original",
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        UpdateQuestionnaireGroupRequest request =
                new UpdateQuestionnaireGroupRequest(
                        "Updated Energy",
                        "Updated description",
                        -1);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        assertThatThrownBy(() ->
                service.updateGroup(
                        groupId.getValue(),
                        request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Display order cannot be negative.");

        verify(groupRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectUpdateWhenGroupNotFound() {

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.empty());

        UpdateQuestionnaireGroupRequest request =
                new UpdateQuestionnaireGroupRequest(
                        "Updated",
                        null,
                        1);

        assertThatThrownBy(() ->
                service.updateGroup(
                        groupId.getValue(),
                        request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Questionnaire group not found:");

        verify(groupRepository, never())
                .save(any());
    }

    @Test
    void shouldActivateGroup() {

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        groupId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        false);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        when(groupRepository.save(any(QuestionnaireGroup.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        QuestionnaireGroupResponse response =
                service.activateGroup(
                        groupId.getValue());

        assertThat(response.active())
                .isTrue();

        verify(groupRepository)
                .save(group);
    }

    @Test
    void shouldDeactivateGroup() {

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        when(groupRepository.save(any(QuestionnaireGroup.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        QuestionnaireGroupResponse response =
                service.deactivateGroup(
                        groupId.getValue());

        assertThat(response.active())
                .isFalse();

        verify(groupRepository)
                .save(group);
    }

    @Test
    void shouldDeleteLeafGroupWithoutVariables() {

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        groupId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        when(groupRepository
                .findByQuestionnaireIdAndParentGroupId(
                        questionnaireId,
                        groupId))
                .thenReturn(List.of());

        when(variableRepository
                .findByQuestionnaireGroupId(groupId))
                .thenReturn(List.of());

        service.deleteGroup(groupId.getValue());

        verify(groupRepository)
                .delete(group);
    }

    @Test
    void shouldRejectDeleteWhenGroupHasChildren() {

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        groupId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        QuestionnaireGroup child =
                QuestionnaireGroup.restore(
                        QuestionnaireGroupId.generate(),
                        questionnaireId,
                        groupId,
                        "POWER",
                        "Power",
                        null,
                        QuestionnaireGroupType.POLICY_GROUP,
                        1,
                        true);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        when(groupRepository
                .findByQuestionnaireIdAndParentGroupId(
                        questionnaireId,
                        groupId))
                .thenReturn(List.of(child));

        assertThatThrownBy(() ->
                service.deleteGroup(groupId.getValue()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Cannot delete questionnaire group because it contains child groups.");

        verify(groupRepository, never())
                .delete(any());
    }

    @Test
    void shouldRejectDeleteWhenGroupHasVariables() {

        QuestionnaireGroup group =
                QuestionnaireGroup.restore(
                        groupId,
                        questionnaireId,
                        null,
                        "ENERGY",
                        "Energy",
                        null,
                        QuestionnaireGroupType.DIMENSION,
                        1,
                        true);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        when(groupRepository
                .findByQuestionnaireIdAndParentGroupId(
                        questionnaireId,
                        groupId))
                .thenReturn(List.of());

        QuestionnaireVariable variable =
                QuestionnaireVariable.create(
                        questionnaireId,
                        groupId,
                        "ENERGY_01",
                        "Energy variable",
                        "Definition",
                        org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType.DECIMAL,
                        "MW",
                        false,
                        1);

        when(variableRepository
                .findByQuestionnaireGroupId(groupId))
                .thenReturn(List.of(variable));

        assertThatThrownBy(() ->
                service.deleteGroup(groupId.getValue()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Cannot delete questionnaire group because it contains variables.");

        verify(groupRepository, never())
                .delete(any());
    }

    @Test
    void shouldRejectDeleteWhenGroupNotFound() {

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.deleteGroup(groupId.getValue()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Questionnaire group not found:");

        verify(groupRepository, never())
                .delete(any());
    }
}
