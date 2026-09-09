package org.afdb.aikp.modules.questionnaire.presentation.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;

public record CreateQuestionnaireVariableRestRequest(

        @NotNull
        UUID questionnaireId,

        UUID questionnaireGroupId,

        @NotBlank
        String seriesCode,

        @NotBlank
        String name,

        String definition,

        @NotNull
        QuestionnaireVariableDataType dataType,

        String unit,

        boolean required,

        int displayOrder
) {
}
