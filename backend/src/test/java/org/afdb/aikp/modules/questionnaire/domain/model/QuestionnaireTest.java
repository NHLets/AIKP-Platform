package org.afdb.aikp.modules.questionnaire.domain.model;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.domain.exception.QuestionnaireLifecycleException;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionnaireTest {

    private Questionnaire createQuestionnaire() {

        return Questionnaire.create(
                QuestionnaireCode.of("PW_TEST"),
                QuestionnaireName.of("Power Template Test"),
                QuestionnaireDescription.of(
                        "Lifecycle test questionnaire"),
                QuestionnaireVersion.of("1.0"),
                DefaultLanguage.of("en"),
                RenderType.FORM);
    }

    @Test
    void shouldCreateQuestionnaireInDraftStatus() {

        Questionnaire questionnaire = createQuestionnaire();

        assertThat(questionnaire.getStatus())
                .isEqualTo(QuestionnaireStatus.DRAFT);

        assertThat(questionnaire.isActive())
                .isTrue();
    }

    @Test
    void shouldSubmitDraftForReview() {

        Questionnaire questionnaire = createQuestionnaire();

        questionnaire.submitForReview();

        assertThat(questionnaire.getStatus())
                .isEqualTo(QuestionnaireStatus.UNDER_REVIEW);
    }

    @Test
    void shouldApproveQuestionnaireUnderReview() {

        Questionnaire questionnaire = createQuestionnaire();

        questionnaire.submitForReview();
        questionnaire.approve();

        assertThat(questionnaire.getStatus())
                .isEqualTo(QuestionnaireStatus.APPROVED);
    }

    @Test
    void shouldPublishApprovedQuestionnaire() {

        Questionnaire questionnaire = createQuestionnaire();

        questionnaire.submitForReview();
        questionnaire.approve();
        questionnaire.publish();

        assertThat(questionnaire.getStatus())
                .isEqualTo(QuestionnaireStatus.PUBLISHED);
    }

    @Test
    void shouldArchiveQuestionnaire() {

        Questionnaire questionnaire = createQuestionnaire();

        questionnaire.submitForReview();
        questionnaire.approve();
        questionnaire.publish();
        questionnaire.archive();

        assertThat(questionnaire.getStatus())
                .isEqualTo(QuestionnaireStatus.ARCHIVED);

        assertThat(questionnaire.isActive())
                .isFalse();
    }

    @Test
    void shouldRejectSubmitForReviewWhenNotDraft() {

        Questionnaire questionnaire = createQuestionnaire();

        questionnaire.submitForReview();

        assertThatThrownBy(questionnaire::submitForReview)
                .isInstanceOf(QuestionnaireLifecycleException.class)
                .hasMessage(
                        "Only draft questionnaires can be submitted for review.");
    }

    @Test
    void shouldRejectApprovalWhenNotUnderReview() {

        Questionnaire questionnaire = createQuestionnaire();

        assertThatThrownBy(questionnaire::approve)
                .isInstanceOf(QuestionnaireLifecycleException.class)
                .hasMessage(
                        "Only questionnaires under review can be approved.");
    }

    @Test
    void shouldRejectPublicationWhenNotApproved() {

        Questionnaire questionnaire = createQuestionnaire();

        assertThatThrownBy(questionnaire::publish)
                .isInstanceOf(QuestionnaireLifecycleException.class)
                .hasMessage(
                        "Only approved questionnaires can be published.");
    }

    @Test
    void shouldActivateQuestionnaire() {

        Questionnaire questionnaire = createQuestionnaire();

        questionnaire.deactivate();
        questionnaire.activate();

        assertThat(questionnaire.isActive())
                .isTrue();
    }

    @Test
    void shouldDeactivateQuestionnaire() {

        Questionnaire questionnaire = createQuestionnaire();

        questionnaire.deactivate();

        assertThat(questionnaire.isActive())
                .isFalse();
    }
}