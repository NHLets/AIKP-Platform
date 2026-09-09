package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.shared.persistence.AuditableEntity;

/**
 * JPA entity representing a statistical variable within a questionnaire.
 */
@Entity
@Table(
        name = "questionnaire_variable",
        schema = "metadata"
)
public class QuestionnaireVariableEntity
        extends AuditableEntity {

    @Column(
            name = "questionnaire_id",
            nullable = false
    )
    private UUID questionnaireId;

    @Column(
            name = "questionnaire_group_id"
    )
    private UUID questionnaireGroupId;

    @Column(
            name = "series_code",
            nullable = false,
            length = 100
    )
    private String seriesCode;

    @Column(
            name = "name",
            nullable = false,
            length = 500
    )
    private String name;

    @Column(
            name = "definition",
            columnDefinition = "TEXT"
    )
    private String definition;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "data_type",
            nullable = false,
            length = 50
    )
    private QuestionnaireVariableDataType dataType;

    @Column(
            name = "unit",
            length = 100
    )
    private String unit;

    @Column(
            name = "required",
            nullable = false
    )
    private boolean required;

    @Column(
            name = "display_order",
            nullable = false
    )
    private int displayOrder;

    @Column(
            name = "active",
            nullable = false
    )
    private boolean active;

    /**
     * Required by JPA.
     */
    protected QuestionnaireVariableEntity() {
        super();
    }

    /**
     * Creates a persistence entity with an existing identifier.
     */
    public QuestionnaireVariableEntity(UUID id) {
        super(id);
    }

    public UUID getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(UUID questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public UUID getQuestionnaireGroupId() {
        return questionnaireGroupId;
    }

    public void setQuestionnaireGroupId(
            UUID questionnaireGroupId) {

        this.questionnaireGroupId =
                questionnaireGroupId;
    }

    public String getSeriesCode() {
        return seriesCode;
    }

    public void setSeriesCode(String seriesCode) {
        this.seriesCode = seriesCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public QuestionnaireVariableDataType getDataType() {
        return dataType;
    }

    public void setDataType(
            QuestionnaireVariableDataType dataType) {

        this.dataType = dataType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
