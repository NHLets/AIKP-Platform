package org.afdb.aikp.modules.questionnaire.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.application.dto.CreateQuestionnaireVariableRequest;
import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireVariableResponse;
import org.afdb.aikp.modules.questionnaire.application.dto.UpdateQuestionnaireVariableRequest;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.exception.QuestionnaireVariableSeriesCodeAlreadyExistsException;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireGroupRepository;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuestionnaireVariableApplicationServiceTest {

    @Mock
    private QuestionnaireVariableRepository variableRepository;

    @Mock
    private QuestionnaireGroupRepository groupRepository;

    private QuestionnaireVariableApplicationService service;

    private QuestionnaireId questionnaireId;

    @BeforeEach
    void setUp() {

        service =
                new QuestionnaireVariableApplicationService(
                        variableRepository,
                        groupRepository);

        questionnaireId =
                QuestionnaireId.generate();
    }

    @Test
    void shouldCreateVariableWithoutGroup() {

        CreateQuestionnaireVariableRequest request =
                createRequest(
                        null,
                        "GDP_001");

        QuestionnaireVariable variable =
                createVariable(
                        null,
                        "GDP_001");

        when(variableRepository
                .existsByQuestionnaireIdAndSeriesCode(
                        questionnaireId,
                        "GDP_001"))
                .thenReturn(false);

        when(variableRepository.save(any(
                QuestionnaireVariable.class)))
                .thenReturn(variable);

        QuestionnaireVariableResponse response =
                service.createVariable(request);

        assertThat(response.seriesCode())
                .isEqualTo("GDP_001");

        assertThat(response.questionnaireId())
                .isEqualTo(questionnaireId.getValue());

        assertThat(response.questionnaireGroupId())
                .isNull();

        verify(variableRepository)
                .save(any(QuestionnaireVariable.class));

        verifyNoInteractions(groupRepository);
    }

    @Test
    void shouldCreateVariableWithGroup() {

        QuestionnaireGroupId groupId =
                QuestionnaireGroupId.generate();

        QuestionnaireGroup group =
                createGroup(
                        questionnaireId,
                        groupId);

        CreateQuestionnaireVariableRequest request =
                createRequest(
                        groupId.getValue(),
                        "GDP_002");

        QuestionnaireVariable variable =
                createVariable(
                        groupId,
                        "GDP_002");

        when(groupRepository.findById(groupId))
                .thenReturn(java.util.Optional.of(group));

        when(variableRepository
                .existsByQuestionnaireIdAndSeriesCode(
                        questionnaireId,
                        "GDP_002"))
                .thenReturn(false);

        when(variableRepository.save(any(
                QuestionnaireVariable.class)))
                .thenReturn(variable);

        QuestionnaireVariableResponse response =
                service.createVariable(request);

        assertThat(response.questionnaireGroupId())
                .isEqualTo(groupId.getValue());

        verify(groupRepository)
                .findById(groupId);

        verify(variableRepository)
                .save(any(QuestionnaireVariable.class));
    }

    @Test
    void shouldRejectVariableWhenSeriesCodeAlreadyExists() {

        CreateQuestionnaireVariableRequest request =
                createRequest(
                        null,
                        "DUPLICATE");

        when(variableRepository
                .existsByQuestionnaireIdAndSeriesCode(
                        questionnaireId,
                        "DUPLICATE"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.createVariable(request))
                .isInstanceOf(
                        QuestionnaireVariableSeriesCodeAlreadyExistsException.class);

        verify(variableRepository, never())
                .save(any(QuestionnaireVariable.class));
    }

    @Test
    void shouldRejectVariableWhenGroupDoesNotExist() {

        QuestionnaireGroupId groupId =
                QuestionnaireGroupId.generate();

        CreateQuestionnaireVariableRequest request =
                createRequest(
                        groupId.getValue(),
                        "GDP_003");

        when(groupRepository.findById(groupId))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() ->
                service.createVariable(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Questionnaire group not found");

        verify(variableRepository, never())
                .save(any(QuestionnaireVariable.class));
    }

    @Test
    void shouldRejectVariableWhenGroupBelongsToAnotherQuestionnaire() {

        QuestionnaireGroupId groupId =
                QuestionnaireGroupId.generate();

        QuestionnaireId anotherQuestionnaireId =
                QuestionnaireId.generate();

        QuestionnaireGroup group =
                createGroup(
                        anotherQuestionnaireId,
                        groupId);

        CreateQuestionnaireVariableRequest request =
                createRequest(
                        groupId.getValue(),
                        "GDP_004");

        when(groupRepository.findById(groupId))
                .thenReturn(java.util.Optional.of(group));

        assertThatThrownBy(() ->
                service.createVariable(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "same questionnaire");

        verify(variableRepository, never())
                .save(any(QuestionnaireVariable.class));
    }

    @Test
    void shouldGetVariableById() {

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.generate();

        QuestionnaireVariable variable =
                createVariable(
                        null,
                        "GET_001");

        when(variableRepository.findById(variableId))
                .thenReturn(java.util.Optional.of(variable));

        QuestionnaireVariableResponse response =
                service.getVariableById(
                        variableId.getValue());

        assertThat(response.seriesCode())
                .isEqualTo("GET_001");
    }

    @Test
    void shouldGetVariablesByQuestionnaire() {

        QuestionnaireVariable variable1 =
                createVariable(
                        null,
                        "VAR_01");

        QuestionnaireVariable variable2 =
                createVariable(
                        null,
                        "VAR_02");

        when(variableRepository.findByQuestionnaireId(
                questionnaireId))
                .thenReturn(
                        List.of(variable1, variable2));

        List<QuestionnaireVariableResponse> responses =
                service.getVariablesByQuestionnaire(
                        questionnaireId.getValue());

        assertThat(responses)
                .extracting(
                        QuestionnaireVariableResponse::seriesCode)
                .containsExactly(
                        "VAR_01",
                        "VAR_02");
    }

    @Test
    void shouldGetVariablesByGroup() {

        QuestionnaireGroupId groupId =
                QuestionnaireGroupId.generate();

        QuestionnaireVariable variable =
                createVariable(
                        groupId,
                        "GROUP_VAR");

        when(variableRepository.findByQuestionnaireGroupId(
                groupId))
                .thenReturn(List.of(variable));

        List<QuestionnaireVariableResponse> responses =
                service.getVariablesByGroup(
                        groupId.getValue());

        assertThat(responses)
                .extracting(
                        QuestionnaireVariableResponse::seriesCode)
                .containsExactly("GROUP_VAR");
    }

    @Test
    void shouldUpdateVariable() {

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.generate();

        QuestionnaireVariable variable =
                createVariable(
                        null,
                        "UPDATE_001");

        UpdateQuestionnaireVariableRequest request =
                new UpdateQuestionnaireVariableRequest(
                        null,
                        "Updated variable",
                        "Updated definition",
                        QuestionnaireVariableDataType.PERCENTAGE,
                        "%",
                        false,
                        5);

        when(variableRepository.findById(variableId))
                .thenReturn(java.util.Optional.of(variable));

        when(variableRepository.save(variable))
                .thenReturn(variable);

        QuestionnaireVariableResponse response =
                service.updateVariable(
                        variableId.getValue(),
                        request);

        assertThat(response.name())
                .isEqualTo("Updated variable");

        assertThat(response.definition())
                .isEqualTo("Updated definition");

        assertThat(response.dataType())
                .isEqualTo(
                        QuestionnaireVariableDataType.PERCENTAGE);

        assertThat(response.unit())
                .isEqualTo("%");

        assertThat(response.required())
                .isFalse();

        assertThat(response.displayOrder())
                .isEqualTo(5);

        assertThat(response.questionnaireGroupId())
                .isNull();

        verify(variableRepository)
                .save(variable);
    }

    @Test
    void shouldAssignVariableToGroupDuringUpdate() {

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.generate();

        QuestionnaireGroupId groupId =
                QuestionnaireGroupId.generate();

        QuestionnaireVariable variable =
                createVariable(
                        null,
                        "UPDATE_GROUP");

        QuestionnaireGroup group =
                createGroup(
                        questionnaireId,
                        groupId);

        UpdateQuestionnaireVariableRequest request =
                new UpdateQuestionnaireVariableRequest(
                        groupId.getValue(),
                        "Grouped variable",
                        null,
                        QuestionnaireVariableDataType.NUMBER,
                        null,
                        true,
                        1);

        when(variableRepository.findById(variableId))
                .thenReturn(java.util.Optional.of(variable));

        when(groupRepository.findById(groupId))
                .thenReturn(java.util.Optional.of(group));

        when(variableRepository.save(variable))
                .thenReturn(variable);

        QuestionnaireVariableResponse response =
                service.updateVariable(
                        variableId.getValue(),
                        request);

        assertThat(response.questionnaireGroupId())
                .isEqualTo(groupId.getValue());

        verify(groupRepository)
                .findById(groupId);

        verify(variableRepository)
                .save(variable);
    }

    @Test
    void shouldActivateVariable() {

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.generate();

        QuestionnaireVariable variable =
                createVariable(
                        null,
                        "ACTIVATE_001");

        variable.deactivate();

        when(variableRepository.findById(variableId))
                .thenReturn(java.util.Optional.of(variable));

        when(variableRepository.save(variable))
                .thenReturn(variable);

        QuestionnaireVariableResponse response =
                service.activateVariable(
                        variableId.getValue());

        assertThat(response.active())
                .isTrue();

        verify(variableRepository)
                .save(variable);
    }

    @Test
    void shouldDeactivateVariable() {

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.generate();

        QuestionnaireVariable variable =
                createVariable(
                        null,
                        "DEACTIVATE_001");

        when(variableRepository.findById(variableId))
                .thenReturn(java.util.Optional.of(variable));

        when(variableRepository.save(variable))
                .thenReturn(variable);

        QuestionnaireVariableResponse response =
                service.deactivateVariable(
                        variableId.getValue());

        assertThat(response.active())
                .isFalse();

        verify(variableRepository)
                .save(variable);
    }

    @Test
    void shouldDeleteVariable() {

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.generate();

        QuestionnaireVariable variable =
                createVariable(
                        null,
                        "DELETE_001");

        when(variableRepository.findById(variableId))
                .thenReturn(java.util.Optional.of(variable));

        service.deleteVariable(
                variableId.getValue());

        verify(variableRepository)
                .delete(variable);
    }

    @Test
    void shouldRejectUnknownVariable() {

        UUID variableId = UUID.randomUUID();

        when(variableRepository.findById(
                QuestionnaireVariableId.of(variableId)))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() ->
                service.getVariableById(variableId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(
                        "Questionnaire variable not found");
    }

    private CreateQuestionnaireVariableRequest createRequest(
            UUID groupId,
            String seriesCode) {

        return new CreateQuestionnaireVariableRequest(
                questionnaireId.getValue(),
                groupId,
                seriesCode,
                "Variable " + seriesCode,
                "Definition " + seriesCode,
                QuestionnaireVariableDataType.NUMBER,
                "USD",
                true,
                0);
    }

    private QuestionnaireVariable createVariable(
            QuestionnaireGroupId groupId,
            String seriesCode) {

        return QuestionnaireVariable.create(
                questionnaireId,
                groupId,
                seriesCode,
                "Variable " + seriesCode,
                "Definition " + seriesCode,
                QuestionnaireVariableDataType.NUMBER,
                "USD",
                true,
                0);
    }

    private QuestionnaireGroup createGroup(
            QuestionnaireId ownerQuestionnaireId,
            QuestionnaireGroupId groupId) {

        return QuestionnaireGroup.restore(
                groupId,
                ownerQuestionnaireId,
                null,
                "GROUP_" + groupId.getValue()
                        .toString()
                        .substring(0, 8),
                "Test Group",
                "Test group",
                QuestionnaireGroupType.GROUP,
                0,
                true);
    }
}
