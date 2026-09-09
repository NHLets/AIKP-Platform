package org.afdb.aikp.modules.questionnaire.presentation.mapper;

import org.springframework.stereotype.Component;

import org.afdb.aikp.modules.questionnaire.application.dto.CreateQuestionnaireVariableRequest;
import org.afdb.aikp.modules.questionnaire.application.dto.UpdateQuestionnaireVariableRequest;
import org.afdb.aikp.modules.questionnaire.presentation.request.CreateQuestionnaireVariableRestRequest;
import org.afdb.aikp.modules.questionnaire.presentation.request.UpdateQuestionnaireVariableRestRequest;

@Component
public class QuestionnaireVariableRestMapper {

    public CreateQuestionnaireVariableRequest toApplicationRequest(
            CreateQuestionnaireVariableRestRequest request) {

        return new CreateQuestionnaireVariableRequest(
                request.questionnaireId(),
                request.questionnaireGroupId(),
                request.seriesCode(),
                request.name(),
                request.definition(),
                request.dataType(),
                request.unit(),
                request.required(),
                request.displayOrder()
        );
    }

    public UpdateQuestionnaireVariableRequest toApplicationRequest(
            UpdateQuestionnaireVariableRestRequest request) {

        return new UpdateQuestionnaireVariableRequest(
                request.questionnaireGroupId(),
                request.name(),
                request.definition(),
                request.dataType(),
                request.unit(),
                request.required(),
                request.displayOrder()
        );
    }
}
