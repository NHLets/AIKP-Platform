package org.afdb.aikp.modules.questionnaire.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.afdb.aikp.modules.questionnaire.application.dto.CreateQuestionnaireVariableRequest;
import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireVariableApplicationMapper;
import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireVariableResponse;
import org.afdb.aikp.modules.questionnaire.application.dto.UpdateQuestionnaireVariableRequest;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.exception.QuestionnaireVariableSeriesCodeAlreadyExistsException;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireGroupRepository;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;

@Service
@Transactional
public class QuestionnaireVariableApplicationService {

    private final QuestionnaireVariableRepository variableRepository;
    private final QuestionnaireGroupRepository groupRepository;

    public QuestionnaireVariableApplicationService(
            QuestionnaireVariableRepository variableRepository,
            QuestionnaireGroupRepository groupRepository) {

        this.variableRepository = variableRepository;
        this.groupRepository = groupRepository;
    }

    public QuestionnaireVariableResponse createVariable(
            CreateQuestionnaireVariableRequest request) {

        QuestionnaireId questionnaireId =
                QuestionnaireId.of(request.questionnaireId());

        QuestionnaireGroupId questionnaireGroupId =
                request.questionnaireGroupId() == null
                        ? null
                        : QuestionnaireGroupId.of(
                                request.questionnaireGroupId());

        validateGroupBelongsToQuestionnaire(
                questionnaireId,
                questionnaireGroupId);

        validateSeriesCodeUniqueness(
                questionnaireId,
                request.seriesCode());

        QuestionnaireVariable variable =
                QuestionnaireVariable.create(
                        questionnaireId,
                        questionnaireGroupId,
                        request.seriesCode(),
                        request.name(),
                        request.definition(),
                        request.dataType(),
                        request.unit(),
                        request.required(),
                        request.displayOrder());

        QuestionnaireVariable saved =
                variableRepository.save(variable);

        return QuestionnaireVariableApplicationMapper
                .toResponse(saved);
    }

    @Transactional(readOnly = true)
    public QuestionnaireVariableResponse getVariableById(
            UUID variableId) {

        QuestionnaireVariable variable =
                findVariable(variableId);

        return QuestionnaireVariableApplicationMapper
                .toResponse(variable);
    }

    @Transactional(readOnly = true)
    public List<QuestionnaireVariableResponse>
            getVariablesByQuestionnaire(
                    UUID questionnaireId) {

        return variableRepository
                .findByQuestionnaireId(
                        QuestionnaireId.of(questionnaireId))
                .stream()
                .map(
                        QuestionnaireVariableApplicationMapper
                                ::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<QuestionnaireVariableResponse>
            getVariablesByGroup(
                    UUID questionnaireGroupId) {

        return variableRepository
                .findByQuestionnaireGroupId(
                        QuestionnaireGroupId.of(
                                questionnaireGroupId))
                .stream()
                .map(
                        QuestionnaireVariableApplicationMapper
                                ::toResponse)
                .toList();
    }

    public QuestionnaireVariableResponse updateVariable(
            UUID variableId,
            UpdateQuestionnaireVariableRequest request) {

        QuestionnaireVariable variable =
                findVariable(variableId);

        QuestionnaireGroupId questionnaireGroupId =
                request.questionnaireGroupId() == null
                        ? null
                        : QuestionnaireGroupId.of(
                                request.questionnaireGroupId());

        validateGroupBelongsToQuestionnaire(
                variable.getQuestionnaireId(),
                questionnaireGroupId);

        if (questionnaireGroupId == null) {
            variable.removeFromGroup();
        } else {
            variable.assignToGroup(questionnaireGroupId);
        }

        variable.rename(request.name());
        variable.changeDefinition(request.definition());
        variable.changeDataType(request.dataType());
        variable.changeUnit(request.unit());

        if (request.required()) {
            variable.markAsRequired();
        } else {
            variable.markAsOptional();
        }

        variable.changeDisplayOrder(
                request.displayOrder());

        QuestionnaireVariable saved =
                variableRepository.save(variable);

        return QuestionnaireVariableApplicationMapper
                .toResponse(saved);
    }

    public QuestionnaireVariableResponse activateVariable(
            UUID variableId) {

        QuestionnaireVariable variable =
                findVariable(variableId);

        variable.activate();

        QuestionnaireVariable saved =
                variableRepository.save(variable);

        return QuestionnaireVariableApplicationMapper
                .toResponse(saved);
    }

    public QuestionnaireVariableResponse deactivateVariable(
            UUID variableId) {

        QuestionnaireVariable variable =
                findVariable(variableId);

        variable.deactivate();

        QuestionnaireVariable saved =
                variableRepository.save(variable);

        return QuestionnaireVariableApplicationMapper
                .toResponse(saved);
    }

    public void deleteVariable(
            UUID variableId) {

        QuestionnaireVariable variable =
                findVariable(variableId);

        variableRepository.delete(variable);
    }

    private QuestionnaireVariable findVariable(
            UUID variableId) {

        return variableRepository
                .findById(
                        QuestionnaireVariableId.of(variableId))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Questionnaire variable not found: "
                                        + variableId));
    }

    private void validateGroupBelongsToQuestionnaire(
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId questionnaireGroupId) {

        if (questionnaireGroupId == null) {
            return;
        }

        QuestionnaireGroup group =
                groupRepository.findById(
                                questionnaireGroupId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Questionnaire group not found: "
                                                + questionnaireGroupId
                                                        .getValue()));

        if (!group.getQuestionnaireId()
                .equals(questionnaireId)) {

            throw new IllegalArgumentException(
                    "Questionnaire group must belong to "
                            + "the same questionnaire.");
        }
    }

    private void validateSeriesCodeUniqueness(
            QuestionnaireId questionnaireId,
            String seriesCode) {

        boolean exists =
                variableRepository
                        .existsByQuestionnaireIdAndSeriesCode(
                                questionnaireId,
                                seriesCode);

        if (exists) {
            throw new QuestionnaireVariableSeriesCodeAlreadyExistsException(
                    seriesCode);
        }
    }
}
