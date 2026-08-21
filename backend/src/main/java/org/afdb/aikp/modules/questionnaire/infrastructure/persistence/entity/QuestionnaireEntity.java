package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.shared.persistence.AuditableEntity;

import java.util.UUID;

/**
 * JPA entity representing a Questionnaire.
 *
 * <p>
 * Persistence concerns are isolated from the Questionnaire
 * domain aggregate.
 * </p>
 *
 * <p>
 * The identifier, audit fields and optimistic locking are inherited
 * from {@link AuditableEntity}.
 * </p>
 */
@Entity
@Table(
        name = "questionnaire",
        schema = "metadata"
)
public class QuestionnaireEntity extends AuditableEntity {

    @Column(
            name = "code",
            nullable = false,
            unique = true,
            length = 50
    )
    private String code;

    @Column(
            name = "name",
            nullable = false,
            length = 255
    )
    private String name;

    @Column(
            name = "description",
            length = 4000
    )
    private String description;

    /**
     * Business version.
     *
     * <p>
     * This must not be confused with the optimistic locking
     * version inherited from {@link AuditableEntity}.
     * </p>
     */
    @Column(
            name = "questionnaire_version",
            nullable = false,
            length = 20
    )
    private String questionnaireVersion;

    @Column(
            name = "default_language",
            nullable = false,
            length = 10
    )
    private String defaultLanguage;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private QuestionnaireStatus status;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "render_type",
            nullable = false,
            length = 30
    )
    private RenderType renderType;

    @Column(
            name = "active",
            nullable = false
    )
    private boolean active;

    /**
     * Required by JPA.
     */
    protected QuestionnaireEntity() {
        super();
    }

    /**
     * Creates a Questionnaire persistence entity
     * with an existing identifier.
     */
    public QuestionnaireEntity(UUID id) {
        super(id);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestionnaireVersion() {
        return questionnaireVersion;
    }

    public void setQuestionnaireVersion(
            String questionnaireVersion) {

        this.questionnaireVersion =
                questionnaireVersion;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(
            String defaultLanguage) {

        this.defaultLanguage =
                defaultLanguage;
    }

    public QuestionnaireStatus getStatus() {
        return status;
    }

    public void setStatus(
            QuestionnaireStatus status) {

        this.status = status;
    }

    public RenderType getRenderType() {
        return renderType;
    }

    public void setRenderType(
            RenderType renderType) {

        this.renderType = renderType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}