package org.afdb.aikp.modules.questionnaire.application.dto;

import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;

public record QuestionnaireVariableResponse(
        UUID id,
        UUID questionnaireId,
        UUID questionnaireGroupId,
        String seriesCode,
        String name,
        String definition,
        QuestionnaireVariableDataType dataType,
        String unit,
        boolean required,
        int displayOrder,
        boolean active) {
}
