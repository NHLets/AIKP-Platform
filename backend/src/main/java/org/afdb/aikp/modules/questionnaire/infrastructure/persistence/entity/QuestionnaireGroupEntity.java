package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;
import org.afdb.aikp.shared.persistence.AuditableEntity;

/**
 * JPA entity representing a structural group within a questionnaire.
 *
 * <p>
 * The hierarchy is represented through {@code parentGroupId}.
 * Persistence concerns are isolated from the domain model.
 * </p>
 */
@Entity
@Table(
        name = "questionnaire_group",
        schema = "metadata"
)
public class QuestionnaireGroupEntity
        extends AuditableEntity {

    @Column(
            name = "questionnaire_id",
            nullable = false
    )
    private UUID questionnaireId;

    @Column(
            name = "parent_group_id"
    )
    private UUID parentGroupId;

    @Column(
            name = "code",
            nullable = false,
            length = 100
    )
    private String code;

    @Column(
            name = "name",
            nullable = false,
            length = 200
    )
    private String name;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "group_type",
            nullable = false,
            length = 50
    )
    private QuestionnaireGroupType groupType;

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
    protected QuestionnaireGroupEntity() {
        super();
    }

    /**
     * Creates a persistence entity with an existing identifier.
     */
    public QuestionnaireGroupEntity(UUID id) {
        super(id);
    }

    public UUID getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(UUID questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public UUID getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(UUID parentGroupId) {
        this.parentGroupId = parentGroupId;
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

    public QuestionnaireGroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(
            QuestionnaireGroupType groupType) {

        this.groupType = groupType;
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
