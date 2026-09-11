package org.afdb.aikp.modules.collection.application.observation.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.afdb.aikp.modules.collection.application.observation.command.CreateDataCollectionObservationCommand;
import org.afdb.aikp.modules.collection.application.observation.command.DeleteDataCollectionObservationCommand;
import org.afdb.aikp.modules.collection.application.observation.command.UpdateDataCollectionObservationCommand;
import org.afdb.aikp.modules.collection.application.observation.mapper.DataCollectionObservationApplicationMapper;
import org.afdb.aikp.modules.collection.application.observation.query.GetDataCollectionObservationQuery;
import org.afdb.aikp.modules.collection.application.observation.query.GetDataCollectionObservationsQuery;
import org.afdb.aikp.modules.collection.application.observation.query.GetObservationByCollectionVariableYearQuery;
import org.afdb.aikp.modules.collection.application.observation.query.GetObservationsByVariableQuery;
import org.afdb.aikp.modules.collection.application.observation.response.DataCollectionObservationResponse;
import org.afdb.aikp.modules.collection.domain.exception.DataCollectionNotFoundException;
import org.afdb.aikp.modules.collection.domain.exception.DataCollectionObservationNotFoundException;
import org.afdb.aikp.modules.collection.domain.exception.DataCollectionObservationNotFoundByKeyException;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionObservationRepository;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionRepository;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;

/**
 * Application service for DataCollectionObservation use cases.
 */
@Service
@Transactional
public class DataCollectionObservationApplicationService {

    private final DataCollectionObservationRepository
            observationRepository;

    private final DataCollectionRepository
            dataCollectionRepository;

    private final QuestionnaireVariableRepository
            questionnaireVariableRepository;

    private final DataCollectionObservationApplicationMapper
            mapper;

    public DataCollectionObservationApplicationService(
            DataCollectionObservationRepository observationRepository,
            DataCollectionRepository dataCollectionRepository,
            QuestionnaireVariableRepository questionnaireVariableRepository,
            DataCollectionObservationApplicationMapper mapper) {

        this.observationRepository =
                observationRepository;
        this.dataCollectionRepository =
                dataCollectionRepository;
        this.questionnaireVariableRepository =
                questionnaireVariableRepository;
        this.mapper = mapper;
    }

    /**
     * Creates a PROVIDED observation.
     */
    public DataCollectionObservationResponse
            createProvidedObservation(
                    CreateDataCollectionObservationCommand command) {

        Objects.requireNonNull(
                command,
                "CreateDataCollectionObservationCommand cannot be null.");

        DataCollectionId dataCollectionId =
                DataCollectionId.of(command.dataCollectionId());

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.of(
                        command.questionnaireVariableId());

        DataCollection dataCollection =
                findDataCollectionOrThrow(dataCollectionId);

        QuestionnaireVariable variable =
                findVariableOrThrow(variableId);

        ensureQuestionnaireConsistency(
                dataCollection,
                variable);

        ensureObservationDoesNotExist(
                dataCollectionId,
                variableId,
                command.referenceYear());

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        dataCollectionId,
                        variableId,
                        command.referenceYear(),
                        variable.getDataType(),
                        command.numericValue(),
                        command.textValue(),
                        command.booleanValue(),
                        command.dateValue(),
                        command.selectedUnit(),
                        command.comment());

        return mapper.toResponse(
                observationRepository.save(observation));
    }

    /**
     * Creates a NOT_AVAILABLE observation.
     */
    public DataCollectionObservationResponse
            createNotAvailableObservation(
                    CreateDataCollectionObservationCommand command) {

        Objects.requireNonNull(
                command,
                "CreateDataCollectionObservationCommand cannot be null.");

        DataCollectionId dataCollectionId =
                DataCollectionId.of(command.dataCollectionId());

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.of(
                        command.questionnaireVariableId());

        DataCollection dataCollection =
                findDataCollectionOrThrow(dataCollectionId);

        QuestionnaireVariable variable =
                findVariableOrThrow(variableId);

        ensureQuestionnaireConsistency(
                dataCollection,
                variable);

        ensureObservationDoesNotExist(
                dataCollectionId,
                variableId,
                command.referenceYear());

        DataCollectionObservation observation =
                DataCollectionObservation.notAvailable(
                        DataCollectionObservationId.generate(),
                        dataCollectionId,
                        variableId,
                        command.referenceYear(),
                        variable.getDataType(),
                        command.selectedUnit(),
                        command.comment());

        return mapper.toResponse(
                observationRepository.save(observation));
    }

    /**
     * Creates a NOT_APPLICABLE observation.
     */
    public DataCollectionObservationResponse
            createNotApplicableObservation(
                    CreateDataCollectionObservationCommand command) {

        Objects.requireNonNull(
                command,
                "CreateDataCollectionObservationCommand cannot be null.");

        DataCollectionId dataCollectionId =
                DataCollectionId.of(command.dataCollectionId());

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.of(
                        command.questionnaireVariableId());

        DataCollection dataCollection =
                findDataCollectionOrThrow(dataCollectionId);

        QuestionnaireVariable variable =
                findVariableOrThrow(variableId);

        ensureQuestionnaireConsistency(
                dataCollection,
                variable);

        ensureObservationDoesNotExist(
                dataCollectionId,
                variableId,
                command.referenceYear());

        DataCollectionObservation observation =
                DataCollectionObservation.notApplicable(
                        DataCollectionObservationId.generate(),
                        dataCollectionId,
                        variableId,
                        command.referenceYear(),
                        variable.getDataType(),
                        command.selectedUnit(),
                        command.comment());

        return mapper.toResponse(
                observationRepository.save(observation));
    }

    /**
     * Updates an existing observation.
     *
     * <p>
     * The observation aggregate is immutable. Therefore the existing
     * observation is reconstructed with the same identifier and status.
     * </p>
     */
    public DataCollectionObservationResponse
            updateObservation(
                    UpdateDataCollectionObservationCommand command) {

        Objects.requireNonNull(
                command,
                "UpdateDataCollectionObservationCommand cannot be null.");

        DataCollectionObservation existing =
                findObservationOrThrow(
                        DataCollectionObservationId.of(
                                command.dataCollectionObservationId()));

        QuestionnaireVariable variable =
                findVariableOrThrow(
                        QuestionnaireVariableId.of(
                                existing.getQuestionnaireVariableId()
                                        .getValue()));

        DataCollectionObservation updated;

        switch (existing.getStatus()) {

            case PROVIDED -> updated =
                    DataCollectionObservation.provided(
                            existing.getId(),
                            existing.getDataCollectionId(),
                            existing.getQuestionnaireVariableId(),
                            existing.getReferenceYear(),
                            variable.getDataType(),
                            command.numericValue(),
                            command.textValue(),
                            command.booleanValue(),
                            command.dateValue(),
                            command.selectedUnit(),
                            command.comment());

            case NOT_AVAILABLE -> updated =
                    DataCollectionObservation.notAvailable(
                            existing.getId(),
                            existing.getDataCollectionId(),
                            existing.getQuestionnaireVariableId(),
                            existing.getReferenceYear(),
                            variable.getDataType(),
                            command.selectedUnit(),
                            command.comment());

            case NOT_APPLICABLE -> updated =
                    DataCollectionObservation.notApplicable(
                            existing.getId(),
                            existing.getDataCollectionId(),
                            existing.getQuestionnaireVariableId(),
                            existing.getReferenceYear(),
                            variable.getDataType(),
                            command.selectedUnit(),
                            command.comment());

            default -> throw new IllegalStateException(
                    "Unsupported observation status: "
                            + existing.getStatus());
        }

        return mapper.toResponse(
                observationRepository.save(updated));
    }

    /**
     * Deletes an observation.
     */
    public void deleteObservation(
            DeleteDataCollectionObservationCommand command) {

        Objects.requireNonNull(
                command,
                "DeleteDataCollectionObservationCommand cannot be null.");

        DataCollectionObservation observation =
                findObservationOrThrow(
                        DataCollectionObservationId.of(
                                command.dataCollectionObservationId()));

        observationRepository.delete(observation);
    }

    /**
     * Retrieves an observation by identifier.
     */
    @Transactional(readOnly = true)
    public DataCollectionObservationResponse
            getObservation(
                    GetDataCollectionObservationQuery query) {

        Objects.requireNonNull(
                query,
                "GetDataCollectionObservationQuery cannot be null.");

        return mapper.toResponse(
                findObservationOrThrow(
                        DataCollectionObservationId.of(
                                query.dataCollectionObservationId())));
    }

    /**
     * Retrieves observations belonging to a DataCollection.
     */
    @Transactional(readOnly = true)
    public List<DataCollectionObservationResponse>
            getObservations(
                    GetDataCollectionObservationsQuery query) {

        Objects.requireNonNull(
                query,
                "GetDataCollectionObservationsQuery cannot be null.");

        DataCollectionId dataCollectionId =
                DataCollectionId.of(query.dataCollectionId());

        ensureDataCollectionExists(dataCollectionId);

        return observationRepository
                .findByDataCollectionId(dataCollectionId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Retrieves observations belonging to a questionnaire variable.
     */
    @Transactional(readOnly = true)
    public List<DataCollectionObservationResponse>
            getObservationsByVariable(
                    GetObservationsByVariableQuery query) {

        Objects.requireNonNull(
                query,
                "GetObservationsByVariableQuery cannot be null.");

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.of(
                        query.questionnaireVariableId());

        findVariableOrThrow(variableId);

        return observationRepository
                .findByQuestionnaireVariableId(variableId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Retrieves an observation by collection, variable and year.
     */
    @Transactional(readOnly = true)
    public DataCollectionObservationResponse
            getObservationByCollectionVariableYear(
                    GetObservationByCollectionVariableYearQuery query) {

        Objects.requireNonNull(
                query,
                "GetObservationByCollectionVariableYearQuery cannot be null.");

        DataCollectionId dataCollectionId =
                DataCollectionId.of(query.dataCollectionId());

        QuestionnaireVariableId variableId =
                QuestionnaireVariableId.of(
                        query.questionnaireVariableId());

        ensureDataCollectionExists(dataCollectionId);
        findVariableOrThrow(variableId);

        return observationRepository
                .findByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId,
                        variableId,
                        query.referenceYear())
                .map(mapper::toResponse)
                .orElseThrow(() ->
                        new DataCollectionObservationNotFoundByKeyException(
                                dataCollectionId.getValue(),
                                variableId.getValue(),
                                query.referenceYear()));
    }

    private DataCollectionObservation findObservationOrThrow(
            DataCollectionObservationId id) {

        return observationRepository
                .findById(id)
                .orElseThrow(() ->
                        new DataCollectionObservationNotFoundException(id));
    }

    private QuestionnaireVariable findVariableOrThrow(
            QuestionnaireVariableId id) {

        return questionnaireVariableRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Questionnaire variable not found with id: "
                                        + id.getValue()));
    }

    private DataCollection findDataCollectionOrThrow(
            DataCollectionId id) {

        return dataCollectionRepository
                .findById(id)
                .orElseThrow(() ->
                        new DataCollectionNotFoundException(id));
    }

    private void ensureDataCollectionExists(
            DataCollectionId id) {

        if (!dataCollectionRepository.existsById(id)) {
            throw new DataCollectionNotFoundException(id);
        }
    }

    private void ensureQuestionnaireConsistency(
            DataCollection dataCollection,
            QuestionnaireVariable variable) {

        if (!dataCollection.getQuestionnaireId()
                .equals(variable.getQuestionnaireId())) {

            throw new IllegalStateException(
                    "Data collection and questionnaire variable "
                            + "belong to different questionnaires.");
        }
    }

    private void ensureObservationDoesNotExist(
            DataCollectionId dataCollectionId,
            QuestionnaireVariableId variableId,
            int referenceYear) {

        if (observationRepository
                .existsByDataCollectionIdAndQuestionnaireVariableIdAndReferenceYear(
                        dataCollectionId,
                        variableId,
                        referenceYear)) {

            throw new IllegalStateException(
                    "Observation already exists for data collection "
                            + dataCollectionId.getValue()
                            + ", questionnaire variable "
                            + variableId.getValue()
                            + " and reference year "
                            + referenceYear);
        }
    }
}
