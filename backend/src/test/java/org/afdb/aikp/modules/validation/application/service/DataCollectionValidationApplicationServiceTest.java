package org.afdb.aikp.modules.validation.application.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;
import org.afdb.aikp.modules.validation.domain.exception.DataCollectionValidationNotFoundException;
import org.afdb.aikp.modules.validation.domain.model.DataCollectionValidation;
import org.afdb.aikp.modules.validation.domain.repository.DataCollectionValidationRepository;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;
import org.afdb.aikp.modules.validation.domain.valueobject.ValidationComments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DataCollectionValidationApplicationServiceTest {

    private DataCollectionValidationRepository validationRepository;
    private DataCollectionRepository dataCollectionRepository;
    private PersonRepository personRepository;
    private DataCollectionValidationApplicationMapper validationMapper;

    private DataCollectionValidationApplicationService service;

    @BeforeEach
    void setUp() {

        validationRepository =
                mock(DataCollectionValidationRepository.class);

        dataCollectionRepository =
                mock(DataCollectionRepository.class);

        personRepository =
                mock(PersonRepository.class);

        validationMapper =
                new DataCollectionValidationApplicationMapper();

        service =
                new DataCollectionValidationApplicationService(
                        validationRepository,
                        dataCollectionRepository,
                        personRepository,
                        validationMapper);
    }

    @Test
    void shouldCreateValidation() {

        UUID dataCollectionUuid = UUID.randomUUID();
        UUID validatorUuid = UUID.randomUUID();
        Instant validatedAt = Instant.now();

        when(dataCollectionRepository.existsById(any()))
                .thenReturn(true);

        when(personRepository.existsById(any()))
                .thenReturn(true);

        when(validationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionValidationResponse response =
                service.create(
                        new CreateDataCollectionValidationCommand(
                                dataCollectionUuid,
                                validatorUuid,
                                ValidationDecision.VALIDATED,
                                "Validation successful",
                                validatedAt));

        assertEquals(dataCollectionUuid,
                response.dataCollectionId());

        assertEquals(validatorUuid,
                response.validatorId());

        assertEquals(ValidationDecision.VALIDATED,
                response.decision());

        assertEquals("Validation successful",
                response.comments());

        verify(validationRepository).save(any());
    }

    @Test
    void shouldRejectCreationWhenDataCollectionDoesNotExist() {

        UUID dataCollectionUuid = UUID.randomUUID();

        when(dataCollectionRepository.existsById(any()))
                .thenReturn(false);

        assertThrows(
                DataCollectionNotFoundException.class,
                () -> service.create(
                        new CreateDataCollectionValidationCommand(
                                dataCollectionUuid,
                                UUID.randomUUID(),
                                ValidationDecision.VALIDATED,
                                null,
                                Instant.now())));

        verify(validationRepository, never()).save(any());
    }

    @Test
    void shouldRejectCreationWhenValidatorDoesNotExist() {

        when(dataCollectionRepository.existsById(any()))
                .thenReturn(true);

        when(personRepository.existsById(any()))
                .thenReturn(false);

        assertThrows(
                PersonNotFoundException.class,
                () -> service.create(
                        new CreateDataCollectionValidationCommand(
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                ValidationDecision.REJECTED,
                                "Missing validator",
                                Instant.now())));

        verify(validationRepository, never()).save(any());
    }

    @Test
    void shouldGetValidationById() {

        DataCollectionValidation validation =
                createValidation();

        when(validationRepository.findById(
                validation.getDataCollectionValidationId()))
                .thenReturn(Optional.of(validation));

        DataCollectionValidationResponse response =
                service.get(
                        new GetDataCollectionValidationQuery(
                                validation
                                        .getDataCollectionValidationId()
                                        .getValue()));

        assertEquals(
                validation
                        .getDataCollectionValidationId()
                        .getValue(),
                response.id());

        assertEquals(
                validation.getDecision(),
                response.decision());
    }

    @Test
    void shouldRejectGetWhenValidationDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(validationRepository.findById(
                DataCollectionValidationId.of(id)))
                .thenReturn(Optional.empty());

        assertThrows(
                DataCollectionValidationNotFoundException.class,
                () -> service.get(
                        new GetDataCollectionValidationQuery(id)));
    }

    @Test
    void shouldGetValidationsByDataCollection() {

        UUID dataCollectionUuid = UUID.randomUUID();

        when(dataCollectionRepository.existsById(any()))
                .thenReturn(true);

        DataCollectionValidation first =
                createValidationFor(dataCollectionUuid);

        DataCollectionValidation second =
                createValidationFor(dataCollectionUuid);

        when(validationRepository.findByDataCollectionId(
                DataCollectionId.of(dataCollectionUuid)))
                .thenReturn(List.of(first, second));

        List<DataCollectionValidationSummary> responses =
                service.getByDataCollection(
                        new GetDataCollectionValidationsByDataCollectionQuery(
                                dataCollectionUuid));

    }

    @Test
    void shouldRejectGetByDataCollectionWhenDataCollectionDoesNotExist() {

        UUID dataCollectionUuid = UUID.randomUUID();

        when(dataCollectionRepository.existsById(any()))
                .thenReturn(false);

        assertThrows(
                DataCollectionNotFoundException.class,
                () -> service.getByDataCollection(
                        new GetDataCollectionValidationsByDataCollectionQuery(
                                dataCollectionUuid)));

        verify(validationRepository, never())
                .findByDataCollectionId(any());
    }

    @Test
    void shouldDeleteValidation() {

        DataCollectionValidation validation =
                createValidation();

        DataCollectionValidationId id =
                validation.getDataCollectionValidationId();

        when(validationRepository.findById(id))
                .thenReturn(Optional.of(validation));

        service.delete(
                new DeleteDataCollectionValidationCommand(
                        id.getValue()));

        verify(validationRepository).delete(validation);
    }

    @Test
    void shouldRejectDeleteWhenValidationDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(validationRepository.findById(
                DataCollectionValidationId.of(id)))
                .thenReturn(Optional.empty());

        assertThrows(
                DataCollectionValidationNotFoundException.class,
                () -> service.delete(
                        new DeleteDataCollectionValidationCommand(id)));
    }

    private DataCollectionValidation createValidation() {

        return DataCollectionValidation.create(
                DataCollectionValidationId.generate(),
                DataCollectionId.generate(),
                PersonId.generate(),
                ValidationDecision.VALIDATED,
                ValidationComments.of("Validation successful"),
                Instant.now());
    }

    private DataCollectionValidation createValidationFor(
            UUID dataCollectionUuid) {

        return DataCollectionValidation.create(
                DataCollectionValidationId.generate(),
                DataCollectionId.of(dataCollectionUuid),
                PersonId.generate(),
                ValidationDecision.VALIDATED,
                ValidationComments.of("Validation successful"),
                Instant.now());
    }
}
