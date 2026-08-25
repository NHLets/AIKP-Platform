package org.afdb.aikp.modules.collection.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataCollectionApplicationServiceTest {

    @Mock
    private DataCollectionRepository dataCollectionRepository;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private QuestionnaireRepository questionnaireRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private DataCollectionApplicationMapper mapper;

    @InjectMocks
    private DataCollectionApplicationService service;

    @Captor
    private ArgumentCaptor<DataCollection> dataCollectionCaptor;

    private UUID campaignUuid;
    private UUID countryUuid;
    private UUID questionnaireUuid;
    private UUID organizationUuid;
    private UUID personUuid;
    private UUID dataCollectionUuid;

    private CampaignId campaignId;
    private CountryId countryId;
    private QuestionnaireId questionnaireId;
    private OrganizationId organizationId;
    private PersonId personId;
    private DataCollectionId dataCollectionId;

    @BeforeEach
    void setUp() {

        campaignUuid = UUID.randomUUID();
        countryUuid = UUID.randomUUID();
        questionnaireUuid = UUID.randomUUID();
        organizationUuid = UUID.randomUUID();
        personUuid = UUID.randomUUID();
        dataCollectionUuid = UUID.randomUUID();

        campaignId = CampaignId.of(campaignUuid);
        countryId = CountryId.of(countryUuid);
        questionnaireId = QuestionnaireId.of(questionnaireUuid);
        organizationId = OrganizationId.of(organizationUuid);
        personId = PersonId.of(personUuid);
        dataCollectionId =
                DataCollectionId.of(dataCollectionUuid);
    }

    // ============================================================
    // CREATE
    // ============================================================

    @Test
    void shouldCreateDataCollectionWhenDependenciesExist() {

        CreateDataCollectionCommand command =
                new CreateDataCollectionCommand(
                        campaignUuid,
                        countryUuid,
                        questionnaireUuid,
                        organizationUuid,
                        personUuid);

        when(campaignRepository.existsById(campaignId))
                .thenReturn(true);

        when(countryRepository.existsById(countryId))
                .thenReturn(true);

        when(questionnaireRepository.existsById(questionnaireId))
                .thenReturn(true);

        when(organizationRepository.existsById(organizationId))
                .thenReturn(true);

        when(personRepository.existsById(personId))
                .thenReturn(true);

        when(dataCollectionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionResponse expected =
                new DataCollectionResponse(
                        UUID.randomUUID(),
                        campaignUuid,
                        countryUuid,
                        questionnaireUuid,
                        organizationUuid,
                        personUuid,
                        DataCollectionStatus.DRAFT);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionResponse result =
                service.createDataCollection(command);

        assertEquals(expected, result);

        verify(dataCollectionRepository)
                .save(dataCollectionCaptor.capture());

        DataCollection saved =
                dataCollectionCaptor.getValue();

        assertEquals(campaignId, saved.getCampaignId());
        assertEquals(countryId, saved.getCountryId());
        assertEquals(questionnaireId, saved.getQuestionnaireId());
        assertEquals(
                organizationId,
                saved.getResponsibleOrganizationId());
        assertEquals(personId, saved.getDataCollectorId());
        assertEquals(
                DataCollectionStatus.DRAFT,
                saved.getStatus());
    }

    @Test
    void shouldThrowCampaignNotFoundExceptionWhenCampaignDoesNotExist() {

        CreateDataCollectionCommand command =
                new CreateDataCollectionCommand(
                        campaignUuid,
                        countryUuid,
                        questionnaireUuid,
                        organizationUuid,
                        personUuid);

        when(campaignRepository.existsById(campaignId))
                .thenReturn(false);

        assertThrows(
                CampaignNotFoundException.class,
                () -> service.createDataCollection(command));

        verify(dataCollectionRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowCountryNotFoundExceptionWhenCountryDoesNotExist() {

        CreateDataCollectionCommand command =
                new CreateDataCollectionCommand(
                        campaignUuid,
                        countryUuid,
                        questionnaireUuid,
                        organizationUuid,
                        personUuid);

        when(campaignRepository.existsById(campaignId))
                .thenReturn(true);

        when(countryRepository.existsById(countryId))
                .thenReturn(false);

        assertThrows(
                CountryNotFoundException.class,
                () -> service.createDataCollection(command));

        verify(dataCollectionRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowQuestionnaireNotFoundExceptionWhenQuestionnaireDoesNotExist() {

        CreateDataCollectionCommand command =
                new CreateDataCollectionCommand(
                        campaignUuid,
                        countryUuid,
                        questionnaireUuid,
                        organizationUuid,
                        personUuid);

        when(campaignRepository.existsById(campaignId))
                .thenReturn(true);

        when(countryRepository.existsById(countryId))
                .thenReturn(true);

        when(questionnaireRepository.existsById(questionnaireId))
                .thenReturn(false);

        assertThrows(
                QuestionnaireNotFoundException.class,
                () -> service.createDataCollection(command));

        verify(dataCollectionRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowOrganizationNotFoundExceptionWhenOrganizationDoesNotExist() {

        CreateDataCollectionCommand command =
                new CreateDataCollectionCommand(
                        campaignUuid,
                        countryUuid,
                        questionnaireUuid,
                        organizationUuid,
                        personUuid);

        when(campaignRepository.existsById(campaignId))
                .thenReturn(true);

        when(countryRepository.existsById(countryId))
                .thenReturn(true);

        when(questionnaireRepository.existsById(questionnaireId))
                .thenReturn(true);

        when(organizationRepository.existsById(organizationId))
                .thenReturn(false);

        assertThrows(
                OrganizationNotFoundException.class,
                () -> service.createDataCollection(command));

        verify(dataCollectionRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowPersonNotFoundExceptionWhenPersonDoesNotExist() {

        CreateDataCollectionCommand command =
                new CreateDataCollectionCommand(
                        campaignUuid,
                        countryUuid,
                        questionnaireUuid,
                        organizationUuid,
                        personUuid);

        when(campaignRepository.existsById(campaignId))
                .thenReturn(true);

        when(countryRepository.existsById(countryId))
                .thenReturn(true);

        when(questionnaireRepository.existsById(questionnaireId))
                .thenReturn(true);

        when(organizationRepository.existsById(organizationId))
                .thenReturn(true);

        when(personRepository.existsById(personId))
                .thenReturn(false);

        assertThrows(
                PersonNotFoundException.class,
                () -> service.createDataCollection(command));

        verify(dataCollectionRepository, never())
                .save(any());
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Test
    void shouldUpdateDataCollection() {

        UUID newOrganizationUuid = UUID.randomUUID();
        UUID newPersonUuid = UUID.randomUUID();

        OrganizationId newOrganizationId =
                OrganizationId.of(newOrganizationUuid);

        PersonId newPersonId =
                PersonId.of(newPersonUuid);

        DataCollection dataCollection =
                createDraftDataCollection();

        UpdateDataCollectionCommand command =
                new UpdateDataCollectionCommand(
                        dataCollectionUuid,
                        newOrganizationUuid,
                        newPersonUuid);

        when(dataCollectionRepository.findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(organizationRepository.existsById(newOrganizationId))
                .thenReturn(true);

        when(personRepository.existsById(newPersonId))
                .thenReturn(true);

        when(dataCollectionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionResponse expected =
                responseFor(
                        dataCollectionUuid,
                        campaignUuid,
                        countryUuid,
                        questionnaireUuid,
                        newOrganizationUuid,
                        newPersonUuid,
                        DataCollectionStatus.DRAFT);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionResponse result =
                service.updateDataCollection(command);

        assertEquals(expected, result);

        assertEquals(
                newOrganizationId,
                dataCollection.getResponsibleOrganizationId());

        assertEquals(
                newPersonId,
                dataCollection.getDataCollectorId());

        verify(dataCollectionRepository)
                .save(dataCollection);
    }

    @Test
    void shouldThrowDataCollectionNotFoundExceptionWhenUpdatingUnknownDataCollection() {

        UpdateDataCollectionCommand command =
                new UpdateDataCollectionCommand(
                        dataCollectionUuid,
                        organizationUuid,
                        personUuid);

        when(dataCollectionRepository.findById(dataCollectionId))
                .thenReturn(Optional.empty());

        assertThrows(
                DataCollectionNotFoundException.class,
                () -> service.updateDataCollection(command));

        verify(dataCollectionRepository, never())
                .save(any());
    }

    // ============================================================
    // LIFECYCLE
    // ============================================================

    @Test
    void shouldStartDataCollection() {

        DataCollection dataCollection =
                createDraftDataCollection();

        when(dataCollectionRepository.findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(dataCollectionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionResponse expected =
                responseWithStatus(
                        DataCollectionStatus.IN_PROGRESS);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionResponse result =
                service.startDataCollection(
                        new StartDataCollectionCommand(
                                dataCollectionUuid));

        assertEquals(expected, result);

        assertEquals(
                DataCollectionStatus.IN_PROGRESS,
                dataCollection.getStatus());

        verify(dataCollectionRepository)
                .save(dataCollection);
    }

    @Test
    void shouldSubmitDataCollection() {

        DataCollection dataCollection =
                createDataCollectionInProgress();

        when(dataCollectionRepository.findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(dataCollectionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionResponse expected =
                responseWithStatus(
                        DataCollectionStatus.SUBMITTED);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionResponse result =
                service.submitDataCollection(
                        new SubmitDataCollectionCommand(
                                dataCollectionUuid));

        assertEquals(expected, result);

        assertEquals(
                DataCollectionStatus.SUBMITTED,
                dataCollection.getStatus());

        verify(dataCollectionRepository)
                .save(dataCollection);
    }

    @Test
    void shouldValidateDataCollection() {

        DataCollection dataCollection =
                createSubmittedDataCollection();

        when(dataCollectionRepository.findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(dataCollectionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionResponse expected =
                responseWithStatus(
                        DataCollectionStatus.VALIDATED);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionResponse result =
                service.validateDataCollection(
                        new ValidateDataCollectionCommand(
                                dataCollectionUuid));

        assertEquals(expected, result);

        assertEquals(
                DataCollectionStatus.VALIDATED,
                dataCollection.getStatus());

        verify(dataCollectionRepository)
                .save(dataCollection);
    }

    @Test
    void shouldRejectDataCollection() {

        DataCollection dataCollection =
                createSubmittedDataCollection();

        when(dataCollectionRepository.findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(dataCollectionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionResponse expected =
                responseWithStatus(
                        DataCollectionStatus.REJECTED);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionResponse result =
                service.rejectDataCollection(
                        new RejectDataCollectionCommand(
                                dataCollectionUuid));

        assertEquals(expected, result);

        assertEquals(
                DataCollectionStatus.REJECTED,
                dataCollection.getStatus());

        verify(dataCollectionRepository)
                .save(dataCollection);
    }

    @Test
    void shouldCancelDataCollection() {

        DataCollection dataCollection =
                createDraftDataCollection();

        when(dataCollectionRepository.findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(dataCollectionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionResponse expected =
                responseWithStatus(
                        DataCollectionStatus.CANCELLED);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionResponse result =
                service.cancelDataCollection(
                        new CancelDataCollectionCommand(
                                dataCollectionUuid));

        assertEquals(expected, result);

        assertEquals(
                DataCollectionStatus.CANCELLED,
                dataCollection.getStatus());

        verify(dataCollectionRepository)
                .save(dataCollection);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Test
    void shouldDeleteDataCollection() {

        DataCollection dataCollection =
                createDraftDataCollection();

        when(dataCollectionRepository.findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        service.deleteDataCollection(
                new DeleteDataCollectionCommand(
                        dataCollectionUuid));

        verify(dataCollectionRepository)
                .delete(dataCollection);
    }

    // ============================================================
    // QUERIES
    // ============================================================

    @Test
    void shouldGetDataCollection() {

        DataCollection dataCollection =
                createDraftDataCollection();

        DataCollectionResponse expected =
                responseWithStatus(
                        DataCollectionStatus.DRAFT);

        when(dataCollectionRepository.findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(mapper.toResponse(dataCollection))
                .thenReturn(expected);

        DataCollectionResponse result =
                service.getDataCollection(
                        new GetDataCollectionQuery(
                                dataCollectionUuid));

        assertEquals(expected, result);
    }

    @Test
    void shouldGetAllDataCollections() {

        DataCollection first =
                createDraftDataCollection();

        DataCollection second =
                DataCollection.create(
                        DataCollectionId.generate(),
                        campaignId,
                        countryId,
                        questionnaireId,
                        organizationId,
                        personId);

        DataCollectionSummary firstSummary =
                summaryFor(first);

        DataCollectionSummary secondSummary =
                summaryFor(second);

        when(dataCollectionRepository.findAll())
                .thenReturn(List.of(first, second));

        when(mapper.toSummary(first))
                .thenReturn(firstSummary);

        when(mapper.toSummary(second))
                .thenReturn(secondSummary);

        List<DataCollectionSummary> result =
                service.getDataCollections(
                        new GetDataCollectionsQuery());

        assertEquals(
                List.of(firstSummary, secondSummary),
                result);
    }

    @Test
    void shouldGetDataCollectionsByCampaign() {

        DataCollection dataCollection =
                createDraftDataCollection();

        DataCollectionSummary expected =
                summaryFor(dataCollection);

        when(campaignRepository.existsById(campaignId))
                .thenReturn(true);

        when(dataCollectionRepository
                .findByCampaignId(campaignId))
                .thenReturn(List.of(dataCollection));

        when(mapper.toSummary(dataCollection))
                .thenReturn(expected);

        List<DataCollectionSummary> result =
                service.getDataCollectionsByCampaign(
                        new GetDataCollectionsByCampaignQuery(
                                campaignUuid));

        assertEquals(List.of(expected), result);

        verify(campaignRepository)
                .existsById(campaignId);
    }

    @Test
    void shouldGetDataCollectionsByCountry() {

        DataCollection dataCollection =
                createDraftDataCollection();

        DataCollectionSummary expected =
                summaryFor(dataCollection);

        when(countryRepository.existsById(countryId))
                .thenReturn(true);

        when(dataCollectionRepository
                .findByCountryId(countryId))
                .thenReturn(List.of(dataCollection));

        when(mapper.toSummary(dataCollection))
                .thenReturn(expected);

        List<DataCollectionSummary> result =
                service.getDataCollectionsByCountry(
                        new GetDataCollectionsByCountryQuery(
                                countryUuid));

        assertEquals(List.of(expected), result);

        verify(countryRepository)
                .existsById(countryId);
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private DataCollection createDraftDataCollection() {

        return DataCollection.create(
                dataCollectionId,
                campaignId,
                countryId,
                questionnaireId,
                organizationId,
                personId);
    }

    private DataCollection createDataCollectionInProgress() {

        DataCollection dataCollection =
                createDraftDataCollection();

        dataCollection.start();

        return dataCollection;
    }

    private DataCollection createSubmittedDataCollection() {

        DataCollection dataCollection =
                createDataCollectionInProgress();

        dataCollection.submit();

        return dataCollection;
    }

    private DataCollectionResponse responseWithStatus(
            DataCollectionStatus status) {

        return responseFor(
                dataCollectionUuid,
                campaignUuid,
                countryUuid,
                questionnaireUuid,
                organizationUuid,
                personUuid,
                status);
    }

    private DataCollectionResponse responseFor(
            UUID id,
            UUID campaign,
            UUID country,
            UUID questionnaire,
            UUID organization,
            UUID person,
            DataCollectionStatus status) {

        return new DataCollectionResponse(
                id,
                campaign,
                country,
                questionnaire,
                organization,
                person,
                status);
    }

    private DataCollectionSummary summaryFor(
            DataCollection dataCollection) {

        return new DataCollectionSummary(
                dataCollection.getDataCollectionId().getValue(),
                dataCollection.getCampaignId().getValue(),
                dataCollection.getCountryId().getValue(),
                dataCollection.getQuestionnaireId().getValue(),
                dataCollection
                        .getResponsibleOrganizationId()
                        .getValue(),
                dataCollection
                        .getDataCollectorId()
                        .getValue(),
                dataCollection.getStatus());
    }
}
