package org.afdb.aikp.modules.questionnaire.application.mapper;

import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireResponse;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireSummary;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;

/**
 * Maps Questionnaire domain aggregates to application DTOs.
 */
public final class QuestionnaireApplicationMapper {

    private QuestionnaireApplicationMapper() {
        // Utility class
    }

    /**
     * Maps a Questionnaire aggregate to a detailed response.
     *
     * @param questionnaire Questionnaire aggregate
     * @return QuestionnaireResponse
     */
    public static QuestionnaireResponse toResponse(
            Questionnaire questionnaire) {

        if (questionnaire == null) {
            return null;
        }

        return new QuestionnaireResponse(
                questionnaire.getId().getValue(),
                questionnaire.getCode().getValue(),
                questionnaire.getName().getValue(),
                questionnaire.getDescription().getValue(),
                questionnaire.getQuestionnaireVersion().getValue(),
                questionnaire.getDefaultLanguage().getValue(),
                questionnaire.getStatus().name(),
                questionnaire.getRenderType().name(),
                questionnaire.isActive()
        );
    }

    /**
     * Maps a Questionnaire aggregate to a summary response.
     *
     * @param questionnaire Questionnaire aggregate
     * @return QuestionnaireSummary
     */
    public static QuestionnaireSummary toSummary(
            Questionnaire questionnaire) {

        if (questionnaire == null) {
            return null;
        }

        return new QuestionnaireSummary(
                questionnaire.getId().getValue(),
                questionnaire.getCode().getValue(),
                questionnaire.getName().getValue(),
                questionnaire.getQuestionnaireVersion().getValue(),
                questionnaire.getStatus().name(),
                questionnaire.isActive()
        );
    }

}