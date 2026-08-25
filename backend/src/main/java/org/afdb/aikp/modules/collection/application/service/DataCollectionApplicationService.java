package org.afdb.aikp.modules.collection.application.service;

import java.util.List;
import java.util.Objects;

import org.afdb.aikp.modules.campaign.domain.exception.CampaignNotFoundException;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;

import org.afdb.aikp.modules.collection.application.command.CancelDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.CreateDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.DeleteDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.RejectDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.StartDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.SubmitDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.UpdateDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.ValidateDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.mapper.DataCollectionApplicationMapper;
import org.afdb.aikp.modules.collection.application.query.GetDataCollectionQuery;
import org.afdb.aikp.modules.collection.application.query.GetDataCollectionsByCampaignQuery;
import org.afdb.aikp.modules.collection.application.query.GetDataCollectionsByCountryQuery;
import org.afdb.aikp.modules.collection.application.query.GetDataCollectionsQuery;
import org.afdb.aikp.modules.collection.application.response.DataCollectionResponse;
import org.afdb.aikp.modules.collection.application.response.DataCollectionSummary;

import org.afdb.aikp.modules.collection.domain.exception.DataCollectionNotFoundException;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionRepository;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;

import org.afdb.aikp.modules.country.domain.exception.CountryNotFoundException;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

import org.afdb.aikp.modules.organization.domain.exception.OrganizationNotFoundException;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;

import org.afdb.aikp.modules.person.domain.exception.PersonNotFoundException;
import org.afdb.aikp.modules.person.domain.repository.PersonRepository;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;

import org.afdb.aikp.modules.questionnaire.domain.exception.QuestionnaireNotFoundException;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for managing DataCollection aggregates.
 */
@Service
@Transactional
public class DataCollectionApplicationService {

    private final DataCollectionRepository dataCollectionRepository;

    private final CampaignRepository campaignRepository;

    private final CountryRepository countryRepository;

    private final QuestionnaireRepository questionnaireRepository;

    private final OrganizationRepository organizationRepository;

    private final PersonRepository personRepository;

    private final DataCollectionApplicationMapper mapper;

    public DataCollectionApplicationService(
            DataCollectionRepository dataCollectionRepository,
            CampaignRepository campaignRepository,
            CountryRepository countryRepository,
            QuestionnaireRepository questionnaireRepository,
            OrganizationRepository organizationRepository,
            PersonRepository personRepository,
            DataCollectionApplicationMapper mapper) {

        this.dataCollectionRepository = Objects.requireNonNull(
                dataCollectionRepository,
                "DataCollectionRepository cannot be null.");

        this.campaignRepository = Objects.requireNonNull(
                campaignRepository,
                "CampaignRepository cannot be null.");

        this.countryRepository = Objects.requireNonNull(
                countryRepository,
                "CountryRepository cannot be null.");

        this.questionnaireRepository = Objects.requireNonNull(
                questionnaireRepository,
                "QuestionnaireRepository cannot be null.");

        this.organizationRepository = Objects.requireNonNull(
                organizationRepository,
                "OrganizationRepository cannot be null.");

        this.personRepository = Objects.requireNonNull(
                personRepository,
                "PersonRepository cannot be null.");

        this.mapper = Objects.requireNonNull(
                mapper,
                "DataCollectionApplicationMapper cannot be null.");
    }

    /**
     * Creates a new DataCollection.
     */
    public DataCollectionResponse createDataCollection(
            CreateDataCollectionCommand command) {

        Objects.requireNonNull(
                command,
                "CreateDataCollectionCommand cannot be null.");

        CampaignId campaignId =
                CampaignId.of(command.campaignId());

        CountryId countryId =
                CountryId.of(command.countryId());

        QuestionnaireId questionnaireId =
                QuestionnaireId.of(command.questionnaireId());

        OrganizationId organizationId =
                OrganizationId.of(
                        command.responsibleOrganizationId());

        PersonId personId =
                PersonId.of(command.dataCollectorId());

        ensureCampaignExists(campaignId);
        ensureCountryExists(countryId);
        ensureQuestionnaireExists(questionnaireId);
        ensureOrganizationExists(organizationId);
        ensurePersonExists(personId);

        DataCollection dataCollection =
                DataCollection.create(
                        DataCollectionId.generate(),
                        campaignId,
                        countryId,
                        questionnaireId,
                        organizationId,
                        personId);

        DataCollection savedDataCollection =
                dataCollectionRepository.save(
                        dataCollection);

        return mapper.toResponse(savedDataCollection);
    }

    /**
     * Updates the responsible organization and data collector.
     */
    public DataCollectionResponse updateDataCollection(
            UpdateDataCollectionCommand command) {

        Objects.requireNonNull(
                command,
                "UpdateDataCollectionCommand cannot be null.");

        DataCollectionId dataCollectionId =
                DataCollectionId.of(
                        command.dataCollectionId());

        DataCollection dataCollection =
                findDataCollectionOrThrow(
                        dataCollectionId);

        OrganizationId organizationId =
                OrganizationId.of(
                        command.responsibleOrganizationId());

        PersonId personId =
                PersonId.of(
                        command.dataCollectorId());

        ensureOrganizationExists(organizationId);
        ensurePersonExists(personId);

        dataCollection.changeResponsibleOrganization(
                organizationId);

        dataCollection.changeDataCollector(personId);

        DataCollection savedDataCollection =
                dataCollectionRepository.save(
                        dataCollection);

        return mapper.toResponse(savedDataCollection);
    }

    /**
     * Starts a draft DataCollection.
     */
    public DataCollectionResponse startDataCollection(
            StartDataCollectionCommand command) {

        Objects.requireNonNull(
                command,
                "StartDataCollectionCommand cannot be null.");

        DataCollection dataCollection =
                findDataCollectionOrThrow(
                        DataCollectionId.of(
                                command.dataCollectionId()));

        dataCollection.start();

        DataCollection savedDataCollection =
                dataCollectionRepository.save(
                        dataCollection);

        return mapper.toResponse(savedDataCollection);
    }

    /**
     * Submits a DataCollection for validation.
     */
    public DataCollectionResponse submitDataCollection(
            SubmitDataCollectionCommand command) {

        Objects.requireNonNull(
                command,
                "SubmitDataCollectionCommand cannot be null.");

        DataCollection dataCollection =
                findDataCollectionOrThrow(
                        DataCollectionId.of(
                                command.dataCollectionId()));

        dataCollection.submit();

        DataCollection savedDataCollection =
                dataCollectionRepository.save(
                        dataCollection);

        return mapper.toResponse(savedDataCollection);
    }

    /**
     * Validates a submitted DataCollection.
     */
    public DataCollectionResponse validateDataCollection(
            ValidateDataCollectionCommand command) {

        Objects.requireNonNull(
                command,
                "ValidateDataCollectionCommand cannot be null.");

        DataCollection dataCollection =
                findDataCollectionOrThrow(
                        DataCollectionId.of(
                                command.dataCollectionId()));

        dataCollection.validate();

        DataCollection savedDataCollection =
                dataCollectionRepository.save(
                        dataCollection);

        return mapper.toResponse(savedDataCollection);
    }

    /**
     * Rejects a submitted DataCollection.
     */
    public DataCollectionResponse rejectDataCollection(
            RejectDataCollectionCommand command) {

        Objects.requireNonNull(
                command,
                "RejectDataCollectionCommand cannot be null.");

        DataCollection dataCollection =
                findDataCollectionOrThrow(
                        DataCollectionId.of(
                                command.dataCollectionId()));

        dataCollection.reject();

        DataCollection savedDataCollection =
                dataCollectionRepository.save(
                        dataCollection);

        return mapper.toResponse(savedDataCollection);
    }

    /**
     * Cancels a DataCollection.
     */
    public DataCollectionResponse cancelDataCollection(
            CancelDataCollectionCommand command) {

        Objects.requireNonNull(
                command,
                "CancelDataCollectionCommand cannot be null.");

        DataCollection dataCollection =
                findDataCollectionOrThrow(
                        DataCollectionId.of(
                                command.dataCollectionId()));

        dataCollection.cancel();

        DataCollection savedDataCollection =
                dataCollectionRepository.save(
                        dataCollection);

        return mapper.toResponse(savedDataCollection);
    }

    /**
     * Deletes a DataCollection.
     */
    public void deleteDataCollection(
            DeleteDataCollectionCommand command) {

        Objects.requireNonNull(
                command,
                "DeleteDataCollectionCommand cannot be null.");

        DataCollection dataCollection =
                findDataCollectionOrThrow(
                        DataCollectionId.of(
                                command.dataCollectionId()));

        dataCollectionRepository.delete(
                dataCollection);
    }

    /**
     * Retrieves a DataCollection by identifier.
     */
    @Transactional(readOnly = true)
    public DataCollectionResponse getDataCollection(
            GetDataCollectionQuery query) {

        Objects.requireNonNull(
                query,
                "GetDataCollectionQuery cannot be null.");

        DataCollection dataCollection =
                findDataCollectionOrThrow(
                        DataCollectionId.of(
                                query.dataCollectionId()));

        return mapper.toResponse(dataCollection);
    }

    /**
     * Retrieves all DataCollections.
     */
    @Transactional(readOnly = true)
    public List<DataCollectionSummary> getDataCollections(
            GetDataCollectionsQuery query) {

        Objects.requireNonNull(
                query,
                "GetDataCollectionsQuery cannot be null.");

        return dataCollectionRepository
                .findAll()
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    /**
     * Retrieves DataCollections belonging to a Campaign.
     */
    @Transactional(readOnly = true)
    public List<DataCollectionSummary>
            getDataCollectionsByCampaign(
                    GetDataCollectionsByCampaignQuery query) {

        Objects.requireNonNull(
                query,
                "GetDataCollectionsByCampaignQuery cannot be null.");

        CampaignId campaignId =
                CampaignId.of(query.campaignId());

        ensureCampaignExists(campaignId);

        return dataCollectionRepository
                .findByCampaignId(campaignId)
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    /**
     * Retrieves DataCollections belonging to a Country.
     */
    @Transactional(readOnly = true)
    public List<DataCollectionSummary>
            getDataCollectionsByCountry(
                    GetDataCollectionsByCountryQuery query) {

        Objects.requireNonNull(
                query,
                "GetDataCollectionsByCountryQuery cannot be null.");

        CountryId countryId =
                CountryId.of(query.countryId());

        ensureCountryExists(countryId);

        return dataCollectionRepository
                .findByCountryId(countryId)
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    private DataCollection findDataCollectionOrThrow(
            DataCollectionId dataCollectionId) {

        return dataCollectionRepository
                .findById(dataCollectionId)
                .orElseThrow(() ->
                        new DataCollectionNotFoundException(
                                dataCollectionId));
    }

    private void ensureCampaignExists(
            CampaignId campaignId) {

        if (!campaignRepository.existsById(campaignId)) {
            throw new CampaignNotFoundException("Campaign not found with id: " + campaignId.getValue());
        }
    }

    private void ensureCountryExists(
            CountryId countryId) {

        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
    }

    private void ensureQuestionnaireExists(
            QuestionnaireId questionnaireId) {

        if (!questionnaireRepository
                .existsById(questionnaireId)) {

            throw new QuestionnaireNotFoundException("Questionnaire not found with id: " + questionnaireId.getValue());
        }
    }

    private void ensureOrganizationExists(
            OrganizationId organizationId) {

        if (!organizationRepository
                .existsById(organizationId)) {

            throw new OrganizationNotFoundException(
                    organizationId);
        }
    }

    private void ensurePersonExists(
            PersonId personId) {

        if (!personRepository.existsById(personId)) {
            throw new PersonNotFoundException(personId);
        }
    }
}
