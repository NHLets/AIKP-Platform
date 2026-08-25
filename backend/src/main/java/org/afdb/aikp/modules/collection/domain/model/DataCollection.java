package org.afdb.aikp.modules.collection.domain.model;

import java.util.Objects;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.shared.domain.AggregateRoot;

/**
 * DataCollection Aggregate Root.
 *
 * <p>
 * Represents an official data collection instance for a specific
 * campaign, country and questionnaire.
 * </p>
 *
 * <p>
 * Each data collection is associated with an organization officially
 * responsible for the collection and a designated person acting as
 * the data collector or enumerator.
 * </p>
 */
public final class DataCollection
        extends AggregateRoot<DataCollectionId> {

    private CampaignId campaignId;

    private CountryId countryId;

    private QuestionnaireId questionnaireId;

    private OrganizationId responsibleOrganizationId;

    private PersonId dataCollectorId;

    private DataCollectionStatus status;

    private DataCollection(
            DataCollectionId id,
            CampaignId campaignId,
            CountryId countryId,
            QuestionnaireId questionnaireId,
            OrganizationId responsibleOrganizationId,
            PersonId dataCollectorId,
            DataCollectionStatus status) {

        super(Objects.requireNonNull(
                id,
                "DataCollection ID cannot be null."));

        this.campaignId = Objects.requireNonNull(
                campaignId,
                "Campaign ID cannot be null.");

        this.countryId = Objects.requireNonNull(
                countryId,
                "Country ID cannot be null.");

        this.questionnaireId = Objects.requireNonNull(
                questionnaireId,
                "Questionnaire ID cannot be null.");

        this.responsibleOrganizationId =
                Objects.requireNonNull(
                        responsibleOrganizationId,
                        "Responsible organization ID cannot be null.");

        this.dataCollectorId =
                Objects.requireNonNull(
                        dataCollectorId,
                        "Data collector ID cannot be null.");

        this.status = Objects.requireNonNull(
                status,
                "DataCollection status cannot be null.");
    }

    /**
     * Creates a new DataCollection.
     */
    public static DataCollection create(
            DataCollectionId id,
            CampaignId campaignId,
            CountryId countryId,
            QuestionnaireId questionnaireId,
            OrganizationId responsibleOrganizationId,
            PersonId dataCollectorId) {

        return new DataCollection(
                id,
                campaignId,
                countryId,
                questionnaireId,
                responsibleOrganizationId,
                dataCollectorId,
                DataCollectionStatus.DRAFT);
    }

    /**
     * Restores an existing DataCollection aggregate.
     */
    public static DataCollection restore(
            DataCollectionId id,
            CampaignId campaignId,
            CountryId countryId,
            QuestionnaireId questionnaireId,
            OrganizationId responsibleOrganizationId,
            PersonId dataCollectorId,
            DataCollectionStatus status) {

        return new DataCollection(
                id,
                campaignId,
                countryId,
                questionnaireId,
                responsibleOrganizationId,
                dataCollectorId,
                status);
    }

    /**
     * Changes the organization responsible for the data collection.
     */
    public void changeResponsibleOrganization(
            OrganizationId responsibleOrganizationId) {

        this.responsibleOrganizationId =
                Objects.requireNonNull(
                        responsibleOrganizationId,
                        "Responsible organization ID cannot be null.");
    }

    /**
     * Changes the designated data collector.
     */
    public void changeDataCollector(
            PersonId dataCollectorId) {

        this.dataCollectorId =
                Objects.requireNonNull(
                        dataCollectorId,
                        "Data collector ID cannot be null.");
    }

    /**
     * Starts the data collection.
     */
    public void start() {

        if (status != DataCollectionStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only a draft data collection can be started.");
        }

        status = DataCollectionStatus.IN_PROGRESS;
    }

    /**
     * Submits the data collection for validation.
     */
    public void submit() {

        if (status != DataCollectionStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Only an in-progress data collection "
                            + "can be submitted.");
        }

        status = DataCollectionStatus.SUBMITTED;
    }

    /**
     * Validates the data collection.
     */
    public void validate() {

        if (status != DataCollectionStatus.SUBMITTED) {
            throw new IllegalStateException(
                    "Only a submitted data collection "
                            + "can be validated.");
        }

        status = DataCollectionStatus.VALIDATED;
    }

    /**
     * Rejects the data collection.
     */
    public void reject() {

        if (status != DataCollectionStatus.SUBMITTED) {
            throw new IllegalStateException(
                    "Only a submitted data collection "
                            + "can be rejected.");
        }

        status = DataCollectionStatus.REJECTED;
    }

    /**
     * Cancels the data collection.
     */
    public void cancel() {

        if (status == DataCollectionStatus.VALIDATED) {
            throw new IllegalStateException(
                    "A validated data collection cannot be cancelled.");
        }

        if (status == DataCollectionStatus.CANCELLED) {
            throw new IllegalStateException(
                    "The data collection is already cancelled.");
        }

        status = DataCollectionStatus.CANCELLED;
    }

    public DataCollectionId getDataCollectionId() {
        return getId();
    }

    public CampaignId getCampaignId() {
        return campaignId;
    }

    public CountryId getCountryId() {
        return countryId;
    }

    public QuestionnaireId getQuestionnaireId() {
        return questionnaireId;
    }

    public OrganizationId getResponsibleOrganizationId() {
        return responsibleOrganizationId;
    }

    public PersonId getDataCollectorId() {
        return dataCollectorId;
    }

    public DataCollectionStatus getStatus() {
        return status;
    }
}
