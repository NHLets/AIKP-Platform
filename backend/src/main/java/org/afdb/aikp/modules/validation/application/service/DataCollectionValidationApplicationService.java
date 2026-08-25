package org.afdb.aikp.modules.validation.application.service;

import java.util.List;

import org.afdb.aikp.modules.collection.domain.exception.DataCollectionNotFoundException;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionRepository;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.person.domain.exception.PersonNotFoundException;
import org.afdb.aikp.modules.person.domain.repository.PersonRepository;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;
import org.afdb.aikp.modules.validation.application.command.CreateDataCollectionValidationCommand;
import org.afdb.aikp.modules.validation.application.command.DeleteDataCollectionValidationCommand;
import org.afdb.aikp.modules.validation.application.mapper.DataCollectionValidationApplicationMapper;
import org.afdb.aikp.modules.validation.application.query.GetDataCollectionValidationQuery;
import org.afdb.aikp.modules.validation.application.query.GetDataCollectionValidationsByDataCollectionQuery;
import org.afdb.aikp.modules.validation.application.response.DataCollectionValidationResponse;
import org.afdb.aikp.modules.validation.application.response.DataCollectionValidationSummary;
import org.afdb.aikp.modules.validation.domain.exception.DataCollectionValidationNotFoundException;
import org.afdb.aikp.modules.validation.domain.model.DataCollectionValidation;
import org.afdb.aikp.modules.validation.domain.repository.DataCollectionValidationRepository;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;
import org.afdb.aikp.modules.validation.domain.valueobject.ValidationComments;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for data collection validations.
 */
@Service
@Transactional
public class DataCollectionValidationApplicationService {

    private final DataCollectionValidationRepository
            validationRepository;

    private final DataCollectionRepository
            dataCollectionRepository;

    private final PersonRepository
            personRepository;

    private final DataCollectionValidationApplicationMapper
            validationMapper;

    public DataCollectionValidationApplicationService(
            DataCollectionValidationRepository validationRepository,
            DataCollectionRepository dataCollectionRepository,
            PersonRepository personRepository,
            DataCollectionValidationApplicationMapper validationMapper) {

        this.validationRepository = validationRepository;
        this.dataCollectionRepository = dataCollectionRepository;
        this.personRepository = personRepository;
        this.validationMapper = validationMapper;
    }

    /**
     * Creates a validation decision.
     */
    public DataCollectionValidationResponse create(
            CreateDataCollectionValidationCommand command) {

        DataCollectionId dataCollectionId =
                DataCollectionId.of(command.dataCollectionId());

        if (!dataCollectionRepository.existsById(
                dataCollectionId)) {

            throw new DataCollectionNotFoundException(
                    dataCollectionId);
        }

        PersonId validatorId =
                PersonId.of(command.validatorId());

        if (!personRepository.existsById(validatorId)) {
            throw new PersonNotFoundException(validatorId);
        }

        DataCollectionValidation validation =
                DataCollectionValidation.create(
                        DataCollectionValidationId.generate(),
                        dataCollectionId,
                        validatorId,
                        command.decision(),
                        ValidationComments.of(
                                command.comments()),
                        command.validatedAt());

        DataCollectionValidation savedValidation =
                validationRepository.save(validation);

        return validationMapper.toResponse(
                savedValidation);
    }

    /**
     * Retrieves a validation by its identifier.
     */
    @Transactional(readOnly = true)
    public DataCollectionValidationResponse get(
            GetDataCollectionValidationQuery query) {

        DataCollectionValidationId id =
                DataCollectionValidationId.of(query.id());

        DataCollectionValidation validation =
                validationRepository.findById(id)
                        .orElseThrow(() ->
                                new DataCollectionValidationNotFoundException(
                                        id));

        return validationMapper.toResponse(validation);
    }

    /**
     * Retrieves all validations for a data collection.
     */
    @Transactional(readOnly = true)
    public List<DataCollectionValidationSummary>
            getByDataCollection(
                    GetDataCollectionValidationsByDataCollectionQuery
                            query) {

        DataCollectionId dataCollectionId =
                DataCollectionId.of(
                        query.dataCollectionId());

        if (!dataCollectionRepository.existsById(
                dataCollectionId)) {

            throw new DataCollectionNotFoundException(
                    dataCollectionId);
        }

        return validationRepository
                .findByDataCollectionId(dataCollectionId)
                .stream()
                .map(validationMapper::toSummary)
                .toList();
    }

    /**
     * Deletes a validation.
     */
    public void delete(
            DeleteDataCollectionValidationCommand command) {

        DataCollectionValidationId id =
                DataCollectionValidationId.of(command.id());

        DataCollectionValidation validation =
                validationRepository.findById(id)
                        .orElseThrow(() ->
                                new DataCollectionValidationNotFoundException(
                                        id));

        validationRepository.delete(validation);
    }
}
