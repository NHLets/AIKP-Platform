package org.afdb.aikp.modules.questionnaire.application.mapper;

import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireResponse;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionnaireApplicationMapperTest {

    private Questionnaire createQuestionnaire() {

        return Questionnaire.create(
                QuestionnaireCode.of("PW_TEST"),
                QuestionnaireName.of("Power Template Test"),
                QuestionnaireDescription.of(
                        "Mapper test questionnaire"),
                QuestionnaireVersion.of("1.0"),
                DefaultLanguage.of("en"),
                RenderType.FORM);
    }

    @Test
    void shouldMapPreviouslySubmittedForReviewAsFalseForNewQuestionnaire() {

        Questionnaire questionnaire = createQuestionnaire();

        QuestionnaireResponse response =
                QuestionnaireApplicationMapper.toResponse(questionnaire);

        assertThat(response.previouslySubmittedForReview())
                .isFalse();
    }

    @Test
    void shouldMapPreviouslySubmittedForReviewAsTrueAfterSubmission() {

        Questionnaire questionnaire = createQuestionnaire();

        questionnaire.submitForReview();

        QuestionnaireResponse response =
                QuestionnaireApplicationMapper.toResponse(questionnaire);

        assertThat(response.previouslySubmittedForReview())
                .isTrue();
    }

}
