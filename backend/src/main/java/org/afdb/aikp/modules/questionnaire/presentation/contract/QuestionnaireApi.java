package org.afdb.aikp.modules.questionnaire.presentation.contract;

import jakarta.validation.Valid;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireResponse;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireSummary;
import org.afdb.aikp.modules.questionnaire.presentation.request.CreateQuestionnaireRequest;
import org.afdb.aikp.modules.questionnaire.presentation.request.UpdateQuestionnaireRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * REST contract for Questionnaire endpoints.
 */
public interface QuestionnaireApi {

    ResponseEntity<QuestionnaireResponse> create(
            @Valid CreateQuestionnaireRequest request);

    ResponseEntity<QuestionnaireResponse> get(
            UUID id);

    ResponseEntity<List<QuestionnaireSummary>> getAll();

    ResponseEntity<List<QuestionnaireSummary>> getActive();

    ResponseEntity<List<QuestionnaireSummary>> getPublished();

    ResponseEntity<QuestionnaireResponse> update(
            UUID id,
            @Valid UpdateQuestionnaireRequest request);

    ResponseEntity<QuestionnaireResponse> activate(
            UUID id);

    ResponseEntity<QuestionnaireResponse> deactivate(
            UUID id);

    ResponseEntity<QuestionnaireResponse> publish(
            UUID id);

    ResponseEntity<QuestionnaireResponse> archive(
            UUID id);

    ResponseEntity<Void> delete(
            UUID id);

}