package org.afdb.aikp.modules.questionnaire.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.afdb.aikp.modules.questionnaire.application.dto.CreateQuestionnaireGroupRequest;
import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireGroupApplicationMapper;
import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireGroupResponse;
import org.afdb.aikp.modules.questionnaire.application.dto.UpdateQuestionnaireGroupRequest;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireGroupRepository;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;

@Service
@Transactional
public class QuestionnaireGroupApplicationService {

    private final QuestionnaireGroupRepository groupRepository;
    private final QuestionnaireVariableRepository variableRepository;

    public QuestionnaireGroupApplicationService(
            QuestionnaireGroupRepository groupRepository,
            QuestionnaireVariableRepository variableRepository) {

        this.groupRepository = groupRepository;
        this.variableRepository = variableRepository;
    }

    public QuestionnaireGroupResponse createGroup(
            CreateQuestionnaireGroupRequest request) {

        QuestionnaireId questionnaireId =
                QuestionnaireId.of(
                        request.questionnaireId());

        QuestionnaireGroupId parentGroupId =
                request.parentGroupId() == null
                        ? null
                        : QuestionnaireGroupId.of(
                                request.parentGroupId());

        validateGroupHierarchy(
                questionnaireId,
                parentGroupId,
                request.groupType());

        validateCodeUniqueness(
                questionnaireId,
                parentGroupId,
                request.code());

        QuestionnaireGroup group =
                QuestionnaireGroup.create(
                        questionnaireId,
                        parentGroupId,
                        request.code(),
                        request.name(),
                        request.description(),
                        request.groupType(),
                        request.displayOrder());

        QuestionnaireGroup saved =
                groupRepository.save(group);

        return QuestionnaireGroupApplicationMapper
                .toResponse(saved);
    }

    @Transactional(readOnly = true)
    public QuestionnaireGroupResponse getGroupById(
            UUID groupId) {

        QuestionnaireGroup group =
                groupRepository.findById(
                                QuestionnaireGroupId.of(groupId))
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Questionnaire group not found: "
                                                + groupId));

        return QuestionnaireGroupApplicationMapper
                .toResponse(group);
    }

    @Transactional(readOnly = true)
    public List<QuestionnaireGroupResponse>
            getGroupsByQuestionnaire(
                    UUID questionnaireId) {

        return groupRepository
                .findByQuestionnaireId(
                        QuestionnaireId.of(questionnaireId))
                .stream()
                .map(
                        QuestionnaireGroupApplicationMapper
                                ::toResponse)
                .toList();
    }



    public QuestionnaireGroupResponse updateGroup(
            UUID groupId,
            UpdateQuestionnaireGroupRequest request) {

        QuestionnaireGroup group =
                groupRepository.findById(
                                QuestionnaireGroupId.of(groupId))
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Questionnaire group not found: "
                                                + groupId));

        group.rename(
                request.name());

        group.changeDescription(
                request.description());

        group.changeDisplayOrder(
                request.displayOrder());

        QuestionnaireGroup saved =
                groupRepository.save(group);

        return QuestionnaireGroupApplicationMapper
                .toResponse(saved);
    }

    public void deleteGroup(
            UUID groupId) {

        QuestionnaireGroupId questionnaireGroupId =
                QuestionnaireGroupId.of(groupId);

        QuestionnaireGroup group =
                groupRepository.findById(questionnaireGroupId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Questionnaire group not found: "
                                                + groupId));

        boolean hasChildren =
                !groupRepository
                        .findByQuestionnaireIdAndParentGroupId(
                                group.getQuestionnaireId(),
                                questionnaireGroupId)
                        .isEmpty();

        if (hasChildren) {
            throw new IllegalStateException(
                    "Cannot delete questionnaire group because "
                            + "it contains child groups.");
        }

        boolean hasVariables =
                !variableRepository
                        .findByQuestionnaireGroupId(
                                questionnaireGroupId)
                        .isEmpty();

        if (hasVariables) {
            throw new IllegalStateException(
                    "Cannot delete questionnaire group because "
                            + "it contains variables.");
        }

        groupRepository.delete(group);
    }

    public QuestionnaireGroupResponse activateGroup(
            UUID groupId) {

        QuestionnaireGroup group =
                groupRepository.findById(
                                QuestionnaireGroupId.of(groupId))
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Questionnaire group not found: "
                                                + groupId));

        group.activate();

        QuestionnaireGroup saved =
                groupRepository.save(group);

        return QuestionnaireGroupApplicationMapper
                .toResponse(saved);
    }

    public QuestionnaireGroupResponse deactivateGroup(
            UUID groupId) {

        QuestionnaireGroup group =
                groupRepository.findById(
                                QuestionnaireGroupId.of(groupId))
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Questionnaire group not found: "
                                                + groupId));

        group.deactivate();

        QuestionnaireGroup saved =
                groupRepository.save(group);

        return QuestionnaireGroupApplicationMapper
                .toResponse(saved);
    }

    private void validateGroupHierarchy(
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId parentGroupId,
            QuestionnaireGroupType groupType) {

        if (parentGroupId == null) {
            return;
        }

        if (groupType == QuestionnaireGroupType.DIMENSION) {
            throw new IllegalArgumentException(
                    "A DIMENSION cannot have a parent group.");
        }

        QuestionnaireGroup parent =
                groupRepository.findById(parentGroupId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Parent questionnaire group not found: "
                                                + parentGroupId.getValue()));

        if (!parent.getQuestionnaireId()
                .equals(questionnaireId)) {

            throw new IllegalArgumentException(
                    "Parent group must belong to the same questionnaire.");
        }
    }

    private void validateCodeUniqueness(
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId parentGroupId,
            String code) {

        boolean exists;

        if (parentGroupId == null) {
            exists = groupRepository
                    .existsRootGroupCode(
                            questionnaireId,
                            code);
        } else {
            exists = groupRepository
                    .existsChildGroupCode(
                            questionnaireId,
                            parentGroupId,
                            code);
        }

        if (exists) {
            throw new IllegalArgumentException(
                    "A questionnaire group with code '"
                            + code
                            + "' already exists in this location.");
        }
    }
}
