package org.afdb.aikp.modules.collection.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;
import org.afdb.aikp.shared.persistence.AuditableEntity;

/**
 * JPA entity representing a collected statistical observation.
 *
 * <p>
 * Audit fields and optimistic locking are inherited from
 * {@link AuditableEntity}.
 * </p>
 */
@Entity
@Table(
        name = "data_collection_observation",
        schema = "reference")
public class DataCollectionObservationEntity
        extends AuditableEntity {

    @Column(
            name = "data_collection_id",
            nullable = false)
    private UUID dataCollectionId;

    @Column(
            name = "questionnaire_variable_id",
            nullable = false)
    private UUID questionnaireVariableId;

    @Column(
            name = "reference_year",
            nullable = false)
    private int referenceYear;

    @Column(
            name = "numeric_value")
    private BigDecimal numericValue;

    @Column(
            name = "text_value")
    private String textValue;

    @Column(
            name = "boolean_value")
    private Boolean booleanValue;

    @Column(
            name = "date_value")
    private LocalDate dateValue;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "observation_status",
            nullable = false,
            length = 30)
    private ObservationStatus status;

    @Column(
            name = "selected_unit",
            length = 100)
    private String selectedUnit;

    @Column(
            name = "comment",
            columnDefinition = "TEXT")
    private String comment;

    /**
     * Required by JPA.
     */
    protected DataCollectionObservationEntity() {
        super();
    }

    /**
     * Creates a persistence entity with an application-assigned UUID.
     */
    public DataCollectionObservationEntity(
            UUID id,
            UUID dataCollectionId,
            UUID questionnaireVariableId,
            int referenceYear,
            ObservationStatus status,
            BigDecimal numericValue,
            String textValue,
            Boolean booleanValue,
            LocalDate dateValue,
            String selectedUnit,
            String comment) {

        super(id);

        this.dataCollectionId = dataCollectionId;
        this.questionnaireVariableId =
                questionnaireVariableId;
        this.referenceYear = referenceYear;
        this.status = status;
        this.numericValue = numericValue;
        this.textValue = textValue;
        this.booleanValue = booleanValue;
        this.dateValue = dateValue;
        this.selectedUnit = selectedUnit;
        this.comment = comment;
    }

    /**
     * Updates the mutable observation values on the existing JPA entity.
     *
     * <p>
     * This method intentionally does not modify the entity identifier,
     * observation key fields, audit fields, or optimistic-locking version.
     * Those fields are managed by JPA/Hibernate.
     * </p>
     */
    public void updateFrom(DataCollectionObservation observation) {
        this.status = observation.getStatus();
        this.numericValue = observation.getNumericValue();
        this.textValue = observation.getTextValue();
        this.booleanValue = observation.getBooleanValue();
        this.dateValue = observation.getDateValue();
        this.selectedUnit = observation.getSelectedUnit();
        this.comment = observation.getComment();
    }

    public UUID getDataCollectionId() {
        return dataCollectionId;
    }

    public UUID getQuestionnaireVariableId() {
        return questionnaireVariableId;
    }

    public int getReferenceYear() {
        return referenceYear;
    }

    public ObservationStatus getStatus() {
        return status;
    }

    public BigDecimal getNumericValue() {
        return numericValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public LocalDate getDateValue() {
        return dateValue;
    }

    public String getSelectedUnit() {
        return selectedUnit;
    }

    public String getComment() {
        return comment;
    }
}
