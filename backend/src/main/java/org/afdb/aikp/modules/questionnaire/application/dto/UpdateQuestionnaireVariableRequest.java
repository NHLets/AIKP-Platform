package org.afdb.aikp.modules.questionnaire.application.dto;

import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;

public record UpdateQuestionnaireVariableRequest(
        UUID questionnaireGroupId,
        String name,
        String definition,
        QuestionnaireVariableDataType dataType,
        String unit,
        boolean required,
        int displayOrder) {
}
