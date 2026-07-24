package org.afdb.aikp.modules.questionnaire.domain.model;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;
import org.afdb.aikp.shared.domain.AggregateRoot;

import java.util.Objects;

/**
 * Aggregate Root representing a Questionnaire.
 *
 * <p>
 * A Questionnaire defines the metadata of a survey instrument used by the
 * AIKP platform. It owns its lifecycle and will later contain Sections,
 * Blocks and Questions.
 * </p>
 */
public class Questionnaire extends AggregateRoot<QuestionnaireId> {

    private final QuestionnaireCode code;

    private QuestionnaireName name;

    private QuestionnaireDescription description;

    /**
     * Business version of the questionnaire.
     * This is NOT the JPA optimistic locking version.
     */
    private QuestionnaireVersion questionnaireVersion;

    private DefaultLanguage defaultLanguage;

    private QuestionnaireStatus status;

    private RenderType renderType;

    private boolean active;

    /**
     * Private constructor.
     */
    private Questionnaire(
            QuestionnaireId id,
            QuestionnaireCode code,
            QuestionnaireName name,
            QuestionnaireDescription description,
            QuestionnaireVersion questionnaireVersion,
            DefaultLanguage defaultLanguage,
            QuestionnaireStatus status,
            RenderType renderType,
            boolean active) {

        super(Objects.requireNonNull(id, "Questionnaire id cannot be null."));

        this.code = Objects.requireNonNull(code,
                "Questionnaire code cannot be null.");

        this.name = Objects.requireNonNull(name,
                "Questionnaire name cannot be null.");

        this.description = Objects.requireNonNull(description,
                "Questionnaire description cannot be null.");

        this.questionnaireVersion = Objects.requireNonNull(
                questionnaireVersion,
                "Questionnaire version cannot be null.");

        this.defaultLanguage = Objects.requireNonNull(
                defaultLanguage,
                "Default language cannot be null.");

        this.status = Objects.requireNonNull(
                status,
                "Questionnaire status cannot be null.");

        this.renderType = Objects.requireNonNull(
                renderType,
                "Render type cannot be null.");

        this.active = active;
    }

    /**
     * Creates a new Questionnaire.
     */
    public static Questionnaire create(
            QuestionnaireCode code,
            QuestionnaireName name,
            QuestionnaireDescription description,
            QuestionnaireVersion questionnaireVersion,
            DefaultLanguage defaultLanguage,
            RenderType renderType) {

        return new Questionnaire(
                QuestionnaireId.generate(),
                code,
                name,
                description,
                questionnaireVersion,
                defaultLanguage,
                QuestionnaireStatus.DRAFT,
                renderType,
                true);
    }

    /**
     * Restores an existing Questionnaire from persistence.
     */
    public static Questionnaire restore(
            QuestionnaireId id,
            QuestionnaireCode code,
            QuestionnaireName name,
            QuestionnaireDescription description,
            QuestionnaireVersion questionnaireVersion,
            DefaultLanguage defaultLanguage,
            QuestionnaireStatus status,
            RenderType renderType,
            boolean active) {

        return new Questionnaire(
                id,
                code,
                name,
                description,
                questionnaireVersion,
                defaultLanguage,
                status,
                renderType,
                active);
    }

    // ---------------------------------------------------------------------
    // Business Behaviour
    // ---------------------------------------------------------------------

    public void rename(QuestionnaireName newName) {
        this.name = Objects.requireNonNull(newName);
    }

    public void changeDescription(
            QuestionnaireDescription newDescription) {

        this.description = Objects.requireNonNull(newDescription);
    }

    public void changeBusinessVersion(
            QuestionnaireVersion newVersion) {

        this.questionnaireVersion = Objects.requireNonNull(newVersion);
    }

    public void changeDefaultLanguage(
            DefaultLanguage language) {

        this.defaultLanguage = Objects.requireNonNull(language);
    }

    public void changeRenderType(RenderType renderType) {

        this.renderType = Objects.requireNonNull(renderType);
    }

    public void submitForReview() {

        if (status != QuestionnaireStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only draft questionnaires can be submitted for review.");
        }

        status = QuestionnaireStatus.UNDER_REVIEW;
    }

    public void approve() {

        if (status != QuestionnaireStatus.UNDER_REVIEW) {
            throw new IllegalStateException(
                    "Only questionnaires under review can be approved.");
        }

        status = QuestionnaireStatus.APPROVED;
    }

    public void publish() {

        if (status != QuestionnaireStatus.APPROVED) {
            throw new IllegalStateException(
                    "Only approved questionnaires can be published.");
        }

        status = QuestionnaireStatus.PUBLISHED;
    }

    public void archive() {
        status = QuestionnaireStatus.ARCHIVED;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    // ---------------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------------

    public QuestionnaireCode getCode() {
        return code;
    }

    public QuestionnaireName getName() {
        return name;
    }

    public QuestionnaireDescription getDescription() {
        return description;
    }

    public QuestionnaireVersion getQuestionnaireVersion() {
        return questionnaireVersion;
    }

    public DefaultLanguage getDefaultLanguage() {
        return defaultLanguage;
    }

    public QuestionnaireStatus getStatus() {
        return status;
    }

    public RenderType getRenderType() {
        return renderType;
    }

    public boolean isActive() {
        return active;
    }
}