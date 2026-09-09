package org.afdb.aikp.modules.questionnaire.application.dto;

import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;

/**
 * Maps QuestionnaireVariable domain objects to application responses.
 */
public final class QuestionnaireVariableApplicationMapper {

    private QuestionnaireVariableApplicationMapper() {
    }

    public static QuestionnaireVariableResponse toResponse(
            QuestionnaireVariable variable) {

        return new QuestionnaireVariableResponse(
                variable.getId().getValue(),
                variable.getQuestionnaireId().getValue(),
                variable.getQuestionnaireGroupId() != null
                        ? variable.getQuestionnaireGroupId().getValue()
                        : null,
                variable.getSeriesCode(),
                variable.getName(),
                variable.getDefinition(),
                variable.getDataType(),
                variable.getUnit(),
                variable.isRequired(),
                variable.getDisplayOrder(),
                variable.isActive()
        );
    }
}
