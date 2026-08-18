package org.afdb.aikp.modules.questionnaire.application.service;

import org.afdb.aikp.modules.questionnaire.application.command.ActivateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.ApproveQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.ArchiveQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.CreateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.DeactivateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.DeleteQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.PublishQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.SubmitQuestionnaireForReviewCommand;
import org.afdb.aikp.modules.questionnaire.application.command.UpdateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.query.GetActiveQuestionnairesQuery;
import org.afdb.aikp.modules.questionnaire.application.query.GetPublishedQuestionnairesQuery;
import org.afdb.aikp.modules.questionnaire.application.query.GetQuestionnaireQuery;
import org.afdb.aikp.modules.questionnaire.application.query.GetQuestionnairesQuery;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireResponse;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireSummary;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.domain.exception.QuestionnaireNotFoundException;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.service.QuestionnaireDomainService;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class QuestionnaireApplicationServiceTest {

    private QuestionnaireRepository repository;

    private QuestionnaireDomainService domainService;

    private QuestionnaireApplicationService service;

    @BeforeEach
    void setUp() {

        repository = mock(QuestionnaireRepository.class);
        domainService = mock(QuestionnaireDomainService.class);

        service = new QuestionnaireApplicationService(
                repository,
                domainService);
    }

    @Test
    void shouldCreateQuestionnaire() {

        CreateQuestionnaireCommand command =
                new CreateQuestionnaireCommand(
                        "PW_TEST",
                        "Power Template Test",
                        "Lifecycle test questionnaire",
                        "1.0",
                        "en",
                        RenderType.FORM);

        when(repository.save(any(Questionnaire.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QuestionnaireResponse response =
                service.create(command);

        assertThat(response.code())
                .isEqualTo("PW_TEST");

        assertThat(response.name())
                .isEqualTo("Power Template Test");

        assertThat(response.status())
                .isEqualTo("DRAFT");

        assertThat(response.active())
                .isTrue();

        verify(domainService)
                .validateCreation(
                        QuestionnaireCode.of("PW_TEST"));

        verify(repository)
                .save(any(Questionnaire.class));
    }

    @Test
    void shouldUpdateQuestionnaire() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_TEST");

        UUID id =
                questionnaire.getId().getValue();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.of(questionnaire));

        when(repository.save(any(Questionnaire.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateQuestionnaireCommand command =
                new UpdateQuestionnaireCommand(
                        id,
                        "Power Template Updated",
                        "Updated description",
                        "1.1",
                        "fr",
                        RenderType.HYBRID);

        QuestionnaireResponse response =
                service.update(command);

        assertThat(response.name())
                .isEqualTo("Power Template Updated");

        assertThat(response.description())
                .isEqualTo("Updated description");

        assertThat(response.version())
                .isEqualTo("1.1");

        assertThat(response.defaultLanguage())
                .isEqualTo("fr");

        assertThat(response.renderType())
                .isEqualTo("HYBRID");

        verify(domainService)
                .validateUpdate(
                        questionnaire.getId(),
                        questionnaire.getCode());

        verify(repository)
                .save(questionnaire);
    }

    @Test
    void shouldSubmitQuestionnaireForReview() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_TEST");

        UUID id =
                questionnaire.getId().getValue();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.of(questionnaire));

        when(repository.save(any(Questionnaire.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QuestionnaireResponse response =
                service.submitForReview(
                        new SubmitQuestionnaireForReviewCommand(id));

        assertThat(response.status())
                .isEqualTo("UNDER_REVIEW");

        verify(repository)
                .save(questionnaire);
    }

    @Test
    void shouldApproveQuestionnaire() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_TEST");

        questionnaire.submitForReview();

        UUID id =
                questionnaire.getId().getValue();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.of(questionnaire));

        when(repository.save(any(Questionnaire.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QuestionnaireResponse response =
                service.approve(
                        new ApproveQuestionnaireCommand(id));

        assertThat(response.status())
                .isEqualTo("APPROVED");

        verify(repository)
                .save(questionnaire);
    }

    @Test
    void shouldActivateQuestionnaire() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_TEST");

        questionnaire.deactivate();

        UUID id =
                questionnaire.getId().getValue();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.of(questionnaire));

        when(repository.save(any(Questionnaire.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QuestionnaireResponse response =
                service.activate(
                        new ActivateQuestionnaireCommand(id));

        assertThat(response.active())
                .isTrue();

        verify(repository)
                .save(questionnaire);
    }

    @Test
    void shouldDeactivateQuestionnaire() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_TEST");

        UUID id =
                questionnaire.getId().getValue();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.of(questionnaire));

        when(repository.save(any(Questionnaire.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QuestionnaireResponse response =
                service.deactivate(
                        new DeactivateQuestionnaireCommand(id));

        assertThat(response.active())
                .isFalse();

        verify(repository)
                .save(questionnaire);
    }

    @Test
    void shouldPublishApprovedQuestionnaire() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_TEST");

        questionnaire.submitForReview();
        questionnaire.approve();

        UUID id =
                questionnaire.getId().getValue();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.of(questionnaire));

        when(repository.save(any(Questionnaire.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QuestionnaireResponse response =
                service.publish(
                        new PublishQuestionnaireCommand(id));

        assertThat(response.status())
                .isEqualTo("PUBLISHED");

        verify(repository)
                .save(questionnaire);
    }

    @Test
    void shouldArchiveQuestionnaire() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_TEST");

        UUID id =
                questionnaire.getId().getValue();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.of(questionnaire));

        when(repository.save(any(Questionnaire.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QuestionnaireResponse response =
                service.archive(
                        new ArchiveQuestionnaireCommand(id));

        assertThat(response.status())
                .isEqualTo("ARCHIVED");

        assertThat(response.active())
                .isFalse();

        verify(repository)
                .save(questionnaire);
    }

    @Test
    void shouldDeleteQuestionnaire() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_TEST");

        UUID id =
                questionnaire.getId().getValue();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.of(questionnaire));

        service.delete(
                new DeleteQuestionnaireCommand(id));

        verify(repository)
                .delete(questionnaire);
    }

    @Test
    void shouldGetQuestionnaireById() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_TEST");

        UUID id =
                questionnaire.getId().getValue();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.of(questionnaire));

        QuestionnaireResponse response =
                service.getById(
                        new GetQuestionnaireQuery(id));

        assertThat(response.id())
                .isEqualTo(id);

        assertThat(response.code())
                .isEqualTo("PW_TEST");

        verify(repository)
                .findById(QuestionnaireId.of(id));
    }

    @Test
    void shouldThrowWhenQuestionnaireDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(repository.findById(
                QuestionnaireId.of(id)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.getById(
                        new GetQuestionnaireQuery(id)))
                .isInstanceOf(
                        QuestionnaireNotFoundException.class)
                .hasMessage(
                        "Questionnaire not found with id: " + id);
    }

    @Test
    void shouldGetAllQuestionnaires() {

        Questionnaire first =
                createQuestionnaire("PW_A");

        Questionnaire second =
                createQuestionnaire("PW_B");

        when(repository.findAll())
                .thenReturn(List.of(first, second));

        List<QuestionnaireSummary> result =
                service.getAll(
                        new GetQuestionnairesQuery());

        assertThat(result)
                .hasSize(2);

        assertThat(result)
                .extracting(QuestionnaireSummary::code)
                .containsExactly("PW_A", "PW_B");

        verify(repository)
                .findAll();
    }

    @Test
    void shouldGetActiveQuestionnaires() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_A");

        when(repository.findActive())
                .thenReturn(List.of(questionnaire));

        List<QuestionnaireSummary> result =
                service.getActive(
                        new GetActiveQuestionnairesQuery());

        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0).code())
                .isEqualTo("PW_A");

        verify(repository)
                .findActive();
    }

    @Test
    void shouldGetPublishedQuestionnaires() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_A");

        questionnaire.submitForReview();
        questionnaire.approve();
        questionnaire.publish();

        when(repository.findByStatus(
                QuestionnaireStatus.PUBLISHED))
                .thenReturn(List.of(questionnaire));

        List<QuestionnaireSummary> result =
                service.getPublished(
                        new GetPublishedQuestionnairesQuery());

        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0).code())
                .isEqualTo("PW_A");

        verify(repository)
                .findByStatus(
                        QuestionnaireStatus.PUBLISHED);
    }

    private Questionnaire createQuestionnaire(
            String code) {

        return Questionnaire.create(
                QuestionnaireCode.of(code),
                QuestionnaireName.of(
                        "Questionnaire " + code),
                QuestionnaireDescription.of(
                        "Test questionnaire"),
                QuestionnaireVersion.of("1.0"),
                DefaultLanguage.of("en"),
                RenderType.FORM);
    }
}