package org.afdb.aikp.modules.questionnaire.presentation.contract;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.application.dto.QuestionnaireVariableResponse;
import org.afdb.aikp.modules.questionnaire.presentation.request.CreateQuestionnaireVariableRestRequest;
import org.afdb.aikp.modules.questionnaire.presentation.request.UpdateQuestionnaireVariableRestRequest;
import org.springframework.http.ResponseEntity;

/**
 * REST contract for Questionnaire Variable endpoints.
 */
public interface QuestionnaireVariableApi {

    ResponseEntity<QuestionnaireVariableResponse> create(
            @Valid CreateQuestionnaireVariableRestRequest request);

    ResponseEntity<QuestionnaireVariableResponse> get(
            UUID id);

    ResponseEntity<List<QuestionnaireVariableResponse>>
            getByQuestionnaire(
                    UUID questionnaireId);

    ResponseEntity<List<QuestionnaireVariableResponse>>
            getByGroup(
                    UUID groupId);

    ResponseEntity<QuestionnaireVariableResponse> update(
            UUID id,
            @Valid UpdateQuestionnaireVariableRestRequest request);

    ResponseEntity<QuestionnaireVariableResponse> activate(
            UUID id);

    ResponseEntity<QuestionnaireVariableResponse> deactivate(
            UUID id);

    ResponseEntity<Void> delete(
            UUID id);
}
