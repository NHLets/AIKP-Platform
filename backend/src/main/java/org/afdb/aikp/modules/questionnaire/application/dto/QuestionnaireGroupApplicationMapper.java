package org.afdb.aikp.modules.questionnaire.application.dto;

import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;

public final class QuestionnaireGroupApplicationMapper {

    private QuestionnaireGroupApplicationMapper() {
    }

    public static QuestionnaireGroupResponse toResponse(
            QuestionnaireGroup group) {

        if (group == null) {
            return null;
        }

        return new QuestionnaireGroupResponse(
                group.getId().getValue(),
                group.getQuestionnaireId().getValue(),
                group.getParentGroupId() == null
                        ? null
                        : group.getParentGroupId().getValue(),
                group.getCode(),
                group.getName(),
                group.getDescription(),
                group.getGroupType(),
                group.getDisplayOrder(),
                group.isActive()
        );
    }
}
