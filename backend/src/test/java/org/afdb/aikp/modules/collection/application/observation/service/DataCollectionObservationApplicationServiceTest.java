package org.afdb.aikp.modules.collection.application.observation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.afdb.aikp.modules.collection.application.observation.command.CreateDataCollectionObservationCommand;
import org.afdb.aikp.modules.collection.application.observation.command.DeleteDataCollectionObservationCommand;
import org.afdb.aikp.modules.collection.application.observation.command.UpdateDataCollectionObservationCommand;
import org.afdb.aikp.modules.collection.application.observation.mapper.DataCollectionObservationApplicationMapper;
import org.afdb.aikp.modules.collection.application.observation.query.GetDataCollectionObservationQuery;
import org.afdb.aikp.modules.collection.application.observation.query.GetDataCollectionObservationsQuery;
import org.afdb.aikp.modules.collection.application.observation.query.GetObservationByCollectionVariableYearQuery;
import org.afdb.aikp.modules.collection.application.observation.query.GetObservationsByVariableQuery;
import org.afdb.aikp.modules.collection.application.observation.response.DataCollectionObservationResponse;

import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;
import org.afdb.aikp.modules.collection.domain.exception.DataCollectionNotFoundException;
import org.afdb.aikp.modules.collection.domain.exception.DataCollectionObservationNotFoundByKeyException;
import org.afdb.aikp.modules.collection.domain.exception.DataCollectionObservationNotFoundException;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionObservationRepository;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionRepository;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataCollectionObservationApplicationServiceTest {

    @Mock
    private DataCollectionObservationRepository observationRepository;
    @Mock
    private DataCollection dataCollection;


    @Mock
    private DataCollectionRepository dataCollectionRepository;

    @Mock
    private QuestionnaireVariableRepository questionnaireVariableRepository;

    @Mock
    private DataCollectionObservationApplicationMapper mapper;

    @InjectMocks
    private DataCollectionObservationApplicationService service;

    @Captor
    private ArgumentCaptor<DataCollectionObservation>
            observationCaptor;

    private UUID dataCollectionUuid;
    private UUID questionnaireVariableUuid;
    private UUID observationUuid;

    private DataCollectionId dataCollectionId;
    private QuestionnaireVariableId questionnaireVariableId;
    private DataCollectionObservationId observationId;

    private QuestionnaireVariable variable;

    @BeforeEach
    void setUp() {

        dataCollectionUuid = UUID.randomUUID();
        observationUuid = UUID.randomUUID();

        dataCollectionId =
                DataCollectionId.of(dataCollectionUuid);

        observationId =
                DataCollectionObservationId.of(
                        observationUuid);

        variable = QuestionnaireVariable.create(
                QuestionnaireId.generate(),
                null,
                "TEST_SERIES",
                "Test variable",
                "Test definition",
                QuestionnaireVariableDataType.DECIMAL,
                "MW",
                false,
                1);

        questionnaireVariableId =
                variable.getId();

        questionnaireVariableUuid =
                questionnaireVariableId.getValue();

    }

    @Test
    void shouldCreateProvidedObservationWhenDependenciesExist() {
        when(dataCollection.getQuestionnaireId())
                .thenReturn(variable.getQuestionnaireId());



        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        new BigDecimal("123.45"),
                        null,
                        null,
                        null,
                        "MW",
                        "Test comment");

        when(dataCollectionRepository
                .findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.of(variable));

        when(observationRepository
                .existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId,
                        questionnaireVariableId,
                        2024))
                .thenReturn(false);

        when(observationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        DataCollectionObservationResponse expected =
                response(
                        observationUuid,
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        ObservationStatus.PROVIDED);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionObservationResponse result =
                service.createProvidedObservation(command);

        assertEquals(expected, result);

        verify(observationRepository)
                .save(observationCaptor.capture());

        DataCollectionObservation saved =
                observationCaptor.getValue();

        assertEquals(
                dataCollectionId,
                saved.getDataCollectionId());

        assertEquals(
                questionnaireVariableId,
                saved.getQuestionnaireVariableId());

        assertEquals(2024, saved.getReferenceYear());

        assertEquals(
                ObservationStatus.PROVIDED,
                saved.getStatus());

        assertEquals(
                new BigDecimal("123.45"),
                saved.getNumericValue());

        assertEquals("MW", saved.getSelectedUnit());
        assertEquals("Test comment", saved.getComment());
    }

    @Test
    void shouldCreateNotAvailableObservation() {
        when(dataCollection.getQuestionnaireId())
                .thenReturn(variable.getQuestionnaireId());



        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2023,
                        null,
                        null,
                        null,
                        null,
                        "MW",
                        "Unavailable");

        when(dataCollectionRepository
                .findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.of(variable));

        when(observationRepository
                .existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId,
                        questionnaireVariableId,
                        2023))
                .thenReturn(false);

        when(observationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        DataCollectionObservationResponse expected =
                response(
                        observationUuid,
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2023,
                        ObservationStatus.NOT_AVAILABLE);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionObservationResponse result =
                service.createNotAvailableObservation(command);

        assertEquals(expected, result);

        verify(observationRepository)
                .save(observationCaptor.capture());

        assertEquals(
                ObservationStatus.NOT_AVAILABLE,
                observationCaptor.getValue().getStatus());
    }

    @Test
    void shouldCreateNotApplicableObservation() {
        when(dataCollection.getQuestionnaireId())
                .thenReturn(variable.getQuestionnaireId());



        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2022,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "Not applicable");

        when(dataCollectionRepository
                .findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.of(variable));

        when(observationRepository
                .existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId,
                        questionnaireVariableId,
                        2022))
                .thenReturn(false);

        when(observationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        DataCollectionObservationResponse expected =
                response(
                        observationUuid,
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2022,
                        ObservationStatus.NOT_APPLICABLE);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionObservationResponse result =
                service.createNotApplicableObservation(command);

        assertEquals(expected, result);

        verify(observationRepository)
                .save(observationCaptor.capture());

        assertEquals(
                ObservationStatus.NOT_APPLICABLE,
                observationCaptor.getValue().getStatus());
    }

    @Test
    void shouldThrowWhenDataCollectionDoesNotExist() {

        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        new BigDecimal("10"),
                        null,
                        null,
                        null,
                        null,
                        null);

        when(dataCollectionRepository
                .findById(dataCollectionId))
                .thenReturn(Optional.empty());

        assertThrows(
                DataCollectionNotFoundException.class,
                () -> service.createProvidedObservation(command));

        verify(observationRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowWhenQuestionnaireVariableDoesNotExist() {

        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        new BigDecimal("10"),
                        null,
                        null,
                        null,
                        null,
                        null);

        when(dataCollectionRepository
                .findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalStateException.class,
                () -> service.createProvidedObservation(command));

        verify(observationRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowWhenObservationAlreadyExists() {
        when(dataCollection.getQuestionnaireId())
                .thenReturn(variable.getQuestionnaireId());



        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        new BigDecimal("10"),
                        null,
                        null,
                        null,
                        null,
                        null);

        when(dataCollectionRepository
                .findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.of(variable));

        when(observationRepository
                .existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId,
                        questionnaireVariableId,
                        2024))
                .thenReturn(true);

        assertThrows(
                IllegalStateException.class,
                () -> service.createProvidedObservation(command));

        verify(observationRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowWhenDataCollectionAndQuestionnaireVariableBelongToDifferentQuestionnaires() {

        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        new BigDecimal("10"),
                        null,
                        null,
                        null,
                        null,
                        null);

        QuestionnaireId differentQuestionnaireId =
                QuestionnaireId.generate();

        when(dataCollectionRepository
                .findById(dataCollectionId))
                .thenReturn(Optional.of(dataCollection));

        when(dataCollection.getQuestionnaireId())
                .thenReturn(differentQuestionnaireId);

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.of(variable));

        assertThrows(
                IllegalStateException.class,
                () -> service.createProvidedObservation(command));

        verify(observationRepository, never())
                .save(any());
    }

    @Test
    void shouldUpdateProvidedObservation() {

        DataCollectionObservation existing =
                DataCollectionObservation.provided(
                        observationId,
                        dataCollectionId,
                        questionnaireVariableId,
                        2024,
                        QuestionnaireVariableDataType.DECIMAL,
                        new BigDecimal("100"),
                        null,
                        null,
                        null,
                        "MW",
                        "Old");

        UpdateDataCollectionObservationCommand command =
                new UpdateDataCollectionObservationCommand(
                        observationUuid,
                        new BigDecimal("200"),
                        null,
                        null,
                        null,
                        "MW",
                        "Updated");

        when(observationRepository.findById(observationId))
                .thenReturn(Optional.of(existing));

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.of(variable));

        when(observationRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        DataCollectionObservationResponse expected =
                response(
                        observationUuid,
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        ObservationStatus.PROVIDED);

        when(mapper.toResponse(any()))
                .thenReturn(expected);

        DataCollectionObservationResponse result =
                service.updateObservation(command);

        assertEquals(expected, result);

        verify(observationRepository)
                .save(observationCaptor.capture());

        DataCollectionObservation updated =
                observationCaptor.getValue();

        assertEquals(
                observationId,
                updated.getId());

        assertEquals(
                ObservationStatus.PROVIDED,
                updated.getStatus());

        assertEquals(
                new BigDecimal("200"),
                updated.getNumericValue());

        assertEquals(
                "Updated",
                updated.getComment());
    }

    @Test
    void shouldDeleteObservation() {

        DataCollectionObservation observation =
                createObservation();

        when(observationRepository.findById(observationId))
                .thenReturn(Optional.of(observation));

        service.deleteObservation(
                new DeleteDataCollectionObservationCommand(
                        observationUuid));

        verify(observationRepository)
                .delete(observation);
    }

    @Test
    void shouldThrowWhenDeletingUnknownObservation() {

        when(observationRepository.findById(observationId))
                .thenReturn(Optional.empty());

        assertThrows(
                DataCollectionObservationNotFoundException.class,
                () -> service.deleteObservation(
                        new DeleteDataCollectionObservationCommand(
                                observationUuid)));

        verify(observationRepository, never())
                .delete(any());
    }

    @Test
    void shouldGetObservationById() {

        DataCollectionObservation observation =
                createObservation();

        when(observationRepository.findById(observationId))
                .thenReturn(Optional.of(observation));

        DataCollectionObservationResponse expected =
                response(
                        observationUuid,
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        ObservationStatus.PROVIDED);

        when(mapper.toResponse(observation))
                .thenReturn(expected);

        DataCollectionObservationResponse result =
                service.getObservation(
                        new GetDataCollectionObservationQuery(
                                observationUuid));

        assertEquals(expected, result);
    }

    @Test
    void shouldGetObservationsByDataCollection() {

        DataCollectionObservation observation =
                createObservation();

        when(dataCollectionRepository
                .existsById(dataCollectionId))
                .thenReturn(true);

        when(observationRepository
                .findByDataCollectionId(dataCollectionId))
                .thenReturn(List.of(observation));

        DataCollectionObservationResponse expected =
                response(
                        observationUuid,
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        ObservationStatus.PROVIDED);

        when(mapper.toResponse(observation))
                .thenReturn(expected);

        List<DataCollectionObservationResponse> result =
                service.getObservations(
                        new GetDataCollectionObservationsQuery(
                                dataCollectionUuid));

        assertEquals(List.of(expected), result);
    }

    @Test
    void shouldGetObservationsByVariable() {

        DataCollectionObservation observation =
                createObservation();

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.of(variable));

        when(observationRepository
                .findByQuestionnaireVariableId(
                        questionnaireVariableId))
                .thenReturn(List.of(observation));

        DataCollectionObservationResponse expected =
                response(
                        observationUuid,
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        ObservationStatus.PROVIDED);

        when(mapper.toResponse(observation))
                .thenReturn(expected);

        List<DataCollectionObservationResponse> result =
                service.getObservationsByVariable(
                        new GetObservationsByVariableQuery(
                                questionnaireVariableUuid));

        assertEquals(List.of(expected), result);
    }

    @Test
    void shouldGetObservationByCompositeKey() {

        DataCollectionObservation observation =
                createObservation();

        when(dataCollectionRepository
                .existsById(dataCollectionId))
                .thenReturn(true);

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.of(variable));

        when(observationRepository
                .findByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId,
                        questionnaireVariableId,
                        2024))
                .thenReturn(Optional.of(observation));

        DataCollectionObservationResponse expected =
                response(
                        observationUuid,
                        dataCollectionUuid,
                        questionnaireVariableUuid,
                        2024,
                        ObservationStatus.PROVIDED);

        when(mapper.toResponse(observation))
                .thenReturn(expected);

        DataCollectionObservationResponse result =
                service.getObservationByCollectionVariableYear(
                        new GetObservationByCollectionVariableYearQuery(
                                dataCollectionUuid,
                                questionnaireVariableUuid,
                                2024));

        assertEquals(expected, result);
    }

    @Test
    void shouldThrowWhenCompositeObservationDoesNotExist() {

        when(dataCollectionRepository
                .existsById(dataCollectionId))
                .thenReturn(true);

        when(questionnaireVariableRepository
                .findById(questionnaireVariableId))
                .thenReturn(Optional.of(variable));

        when(observationRepository
                .findByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId,
                        questionnaireVariableId,
                        2024))
                .thenReturn(Optional.empty());

        assertThrows(
                DataCollectionObservationNotFoundByKeyException.class,
                () -> service.getObservationByCollectionVariableYear(
                        new GetObservationByCollectionVariableYearQuery(
                                dataCollectionUuid,
                                questionnaireVariableUuid,
                                2024)));
    }

    private DataCollectionObservation createObservation() {

        return DataCollectionObservation.provided(
                observationId,
                dataCollectionId,
                questionnaireVariableId,
                2024,
                QuestionnaireVariableDataType.DECIMAL,
                new BigDecimal("100"),
                null,
                null,
                null,
                "MW",
                "Test");
    }

    private DataCollectionObservationResponse response(
            UUID id,
            UUID dataCollectionId,
            UUID questionnaireVariableId,
            int referenceYear,
            ObservationStatus status) {

        return new DataCollectionObservationResponse(
                id,
                dataCollectionId,
                questionnaireVariableId,
                referenceYear,
                status,
                null,
                null,
                null,
                null,
                null,
                null);
    }

}
