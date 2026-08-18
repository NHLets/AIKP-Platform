package org.afdb.aikp.modules.questionnaire.application.service;

import org.afdb.aikp.modules.questionnaire.application.command.ActivateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.ArchiveQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.CreateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.DeactivateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.DeleteQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.PublishQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.command.UpdateQuestionnaireCommand;
import org.afdb.aikp.modules.questionnaire.application.mapper.QuestionnaireApplicationMapper;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.afdb.aikp.modules.questionnaire.application.query.GetActiveQuestionnairesQuery;
import org.afdb.aikp.modules.questionnaire.application.query.GetPublishedQuestionnairesQuery;
import org.afdb.aikp.modules.questionnaire.application.query.GetQuestionnaireQuery;
import org.afdb.aikp.modules.questionnaire.application.query.GetQuestionnairesQuery;
import org.afdb.aikp.modules.questionnaire.application.response.QuestionnaireSummary;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.application.command.SubmitQuestionnaireForReviewCommand;
import org.afdb.aikp.modules.questionnaire.application.command.ApproveQuestionnaireCommand;

import java.util.List;
@Service
@Transactional
public class QuestionnaireApplicationService {

    private final QuestionnaireRepository repository;

    private final QuestionnaireDomainService domainService;

    public QuestionnaireApplicationService(
            QuestionnaireRepository repository,
            QuestionnaireDomainService domainService) {

        this.repository = repository;
        this.domainService = domainService;
    }

    /**
     * Creates a new questionnaire.
     */
    public QuestionnaireResponse create(
            CreateQuestionnaireCommand command) {

        QuestionnaireCode code =
                QuestionnaireCode.of(command.code());

        domainService.validateCreation(code);

        Questionnaire questionnaire = Questionnaire.create(
                code,
                QuestionnaireName.of(command.name()),
                QuestionnaireDescription.of(command.description()),
                QuestionnaireVersion.of(command.version()),
                DefaultLanguage.of(command.defaultLanguage()),
                command.renderType());

        repository.save(questionnaire);

        return QuestionnaireApplicationMapper.toResponse(questionnaire);
    }

    /**
     * Updates questionnaire metadata.
     */
    public QuestionnaireResponse update(
            UpdateQuestionnaireCommand command) {

        Questionnaire questionnaire =
                findByIdOrThrow(command.id());

        domainService.validateUpdate(
                questionnaire.getId(),
                questionnaire.getCode());

        questionnaire.rename(
                QuestionnaireName.of(command.name()));

        questionnaire.changeDescription(
                QuestionnaireDescription.of(command.description()));

        questionnaire.changeBusinessVersion(
                QuestionnaireVersion.of(command.version()));

        questionnaire.changeDefaultLanguage(
                DefaultLanguage.of(command.defaultLanguage()));

        questionnaire.changeRenderType(
                command.renderType());

        repository.save(questionnaire);

        return QuestionnaireApplicationMapper.toResponse(questionnaire);
    }

    /**
 * Submits a questionnaire for review.
 */
        public QuestionnaireResponse submitForReview(
        SubmitQuestionnaireForReviewCommand command) {

    Questionnaire questionnaire =
            findByIdOrThrow(command.id());

    questionnaire.submitForReview();

    repository.save(questionnaire);

    return QuestionnaireApplicationMapper.toResponse(questionnaire);
        }

    /**
 * Approves a questionnaire that is under review.
 */
        public QuestionnaireResponse approve(
        ApproveQuestionnaireCommand command) {

    Questionnaire questionnaire =
            findByIdOrThrow(command.id());

    questionnaire.approve();

    repository.save(questionnaire);

    return QuestionnaireApplicationMapper.toResponse(questionnaire);
        }

        /**
     * Activates a questionnaire.
     */
    public QuestionnaireResponse activate(
            ActivateQuestionnaireCommand command) {

        Questionnaire questionnaire =
                findByIdOrThrow(command.id());

        questionnaire.activate();

        repository.save(questionnaire);

        return QuestionnaireApplicationMapper.toResponse(questionnaire);
    }

    /**
     * Deactivates a questionnaire.
     */
    public QuestionnaireResponse deactivate(
            DeactivateQuestionnaireCommand command) {

        Questionnaire questionnaire =
                findByIdOrThrow(command.id());

        questionnaire.deactivate();

        repository.save(questionnaire);

        return QuestionnaireApplicationMapper.toResponse(questionnaire);
    }

    /**
     * Publishes a questionnaire.
     */
    public QuestionnaireResponse publish(
            PublishQuestionnaireCommand command) {

        Questionnaire questionnaire =
                findByIdOrThrow(command.id());

        questionnaire.publish();

        repository.save(questionnaire);

        return QuestionnaireApplicationMapper.toResponse(questionnaire);
    }

    /**
     * Archives a questionnaire.
     */
    public QuestionnaireResponse archive(
            ArchiveQuestionnaireCommand command) {

        Questionnaire questionnaire =
                findByIdOrThrow(command.id());

        questionnaire.archive();

        repository.save(questionnaire);

        return QuestionnaireApplicationMapper.toResponse(questionnaire);
    }

    /**
     * Deletes a questionnaire.
     */
    public void delete(
            DeleteQuestionnaireCommand command) {

        Questionnaire questionnaire =
                findByIdOrThrow(command.id());

        repository.delete(questionnaire);
    }
    /**
     * Returns one questionnaire.
     */
    @Transactional(readOnly = true)
    public QuestionnaireResponse getById(
            GetQuestionnaireQuery query) {

        Questionnaire questionnaire =
                findByIdOrThrow(query.id());

        return QuestionnaireApplicationMapper.toResponse(questionnaire);
    }

    /**
     * Returns all questionnaires.
     */
    @Transactional(readOnly = true)
    public List<QuestionnaireSummary> getAll(
            GetQuestionnairesQuery query) {

        return repository.findAll()
                .stream()
                .map(QuestionnaireApplicationMapper::toSummary)
                .toList();
    }

    /**
     * Returns all active questionnaires.
     */
    @Transactional(readOnly = true)
    public List<QuestionnaireSummary> getActive(
            GetActiveQuestionnairesQuery query) {

        return repository.findActive()
                .stream()
                .map(QuestionnaireApplicationMapper::toSummary)
                .toList();
    }

    /**
     * Returns all published questionnaires.
     */
    @Transactional(readOnly = true)
    public List<QuestionnaireSummary> getPublished(
            GetPublishedQuestionnairesQuery query) {

        return repository.findByStatus(
                        QuestionnaireStatus.PUBLISHED)
                .stream()
                .map(QuestionnaireApplicationMapper::toSummary)
                .toList();
    }
    /**
     * Finds a questionnaire by its identifier or throws an exception.
     *
     * @param id Questionnaire identifier
     * @return Questionnaire aggregate
     * @throws QuestionnaireNotFoundException if not found
     */
    private Questionnaire findByIdOrThrow(
            java.util.UUID id) {

        return repository.findById(
                        QuestionnaireId.of(id))
                .orElseThrow(() ->
                        new QuestionnaireNotFoundException(
                                "Questionnaire not found with id: " + id));
    }

}