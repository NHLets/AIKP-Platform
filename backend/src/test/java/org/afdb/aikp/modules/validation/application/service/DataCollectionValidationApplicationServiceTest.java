package org.afdb.aikp.modules.validation.application.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
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
import org.afdb.aikp.modules.validation.domain.exception.DataCollectionValidationNotAllowedException;
import org.afdb.aikp.modules.validation.domain.model.DataCollectionValidation;
import org.afdb.aikp.modules.validation.domain.repository.DataCollectionValidationRepository;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;
import org.afdb.aikp.modules.validation.domain.valueobject.ValidationComments;

import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private UserRepository userRepository;
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

        userRepository = mock(UserRepository.class);

        validationMapper =
                new DataCollectionValidationApplicationMapper();

        service =
                new DataCollectionValidationApplicationService(
                        validationRepository,
                        dataCollectionRepository,
                        personRepository,
                        validationMapper,
                        userRepository);
    }


    @Test
    void shouldCreateValidationForAuthenticatedIamUser() {
        UUID collectionUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        String email = "validator@example.org";
        Instant validatedAt = Instant.parse("2026-10-15T12:00:00Z");

        DataCollection collection =
                createDataCollection(collectionUuid, DataCollectionStatus.SUBMITTED);

        User user = User.create(
                Username.of("validator01"),
                Email.of(email),
                FullName.of("Test Validator"),
                PasswordHash.of("$2a$10$abcdefghijklmnopqrstuvwxyz0123456789012345678901234567890"));

        when(userRepository.findByEmail(Email.of(email)))
                .thenReturn(Optional.of(user));
        when(dataCollectionRepository.findById(any()))
                .thenReturn(Optional.of(collection));
        when(validationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionValidationResponse response =
                service.createForAuthenticatedUser(
                        collectionUuid,
                        email,
                        ValidationDecision.VALIDATED,
                        "IAM validation",
                        validatedAt);

        assertEquals(collectionUuid, response.dataCollectionId());
        assertEquals(user.getId().getValue(), response.validatorUserId());
        assertEquals(ValidationDecision.VALIDATED, response.decision());
        assertEquals("IAM validation", response.comments());
        assertEquals(DataCollectionStatus.VALIDATED, collection.getStatus());

        verify(userRepository).findByEmail(Email.of(email));
        verify(dataCollectionRepository).save(collection);
        verify(validationRepository).save(argThat(validation ->
                validation.getValidatorUserId() != null
                        && validation.getValidatorUserId().equals(user.getId())
                        && validation.getValidatorId() == null));
    }

    @Test
    void shouldRejectAuthenticatedValidationWhenIamUserDoesNotExist() {
        String email = "missing@example.org";

        when(userRepository.findByEmail(Email.of(email)))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.createForAuthenticatedUser(
                        UUID.randomUUID(),
                        email,
                        ValidationDecision.VALIDATED,
                        "IAM validation",
                        Instant.now()));

        verify(dataCollectionRepository, never()).save(any());
        verify(validationRepository, never()).save(any());
    }

    @Test
    void shouldRejectAuthenticatedValidationWhenCollectionIsNotSubmitted() {
        UUID collectionUuid = UUID.randomUUID();
        String email = "validator@example.org";

        User user = User.create(
                Username.of("validator01"),
                Email.of(email),
                FullName.of("Test Validator"),
                PasswordHash.of("$2a$10$abcdefghijklmnopqrstuvwxyz0123456789012345678901234567890"));

        DataCollection collection =
                createDataCollection(collectionUuid, DataCollectionStatus.DRAFT);

        when(userRepository.findByEmail(Email.of(email)))
                .thenReturn(Optional.of(user));
        when(dataCollectionRepository.findById(any()))
                .thenReturn(Optional.of(collection));

        assertThrows(
                DataCollectionValidationNotAllowedException.class,
                () -> service.createForAuthenticatedUser(
                        collectionUuid,
                        email,
                        ValidationDecision.VALIDATED,
                        "IAM validation",
                        Instant.now()));

        verify(dataCollectionRepository, never()).save(any());
        verify(validationRepository, never()).save(any());
    }

    @Test
    void shouldCreateValidation() {

        UUID dataCollectionUuid = UUID.randomUUID();
        UUID validatorUuid = UUID.randomUUID();
        Instant validatedAt = Instant.now();

        DataCollection dataCollection =
                createDataCollection(
                        dataCollectionUuid,
                        DataCollectionStatus.SUBMITTED);

        when(dataCollectionRepository.findById(any()))
                .thenReturn(Optional.of(dataCollection));

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

        verify(dataCollectionRepository).save(dataCollection);
        verify(validationRepository).save(any());
    }

    @Test
    void shouldCreateRejectedValidationAndRejectDataCollection() {

        UUID dataCollectionUuid = UUID.randomUUID();

        DataCollection dataCollection =
                createDataCollection(
                        dataCollectionUuid,
                        DataCollectionStatus.SUBMITTED);

        when(dataCollectionRepository.findById(any()))
                .thenReturn(Optional.of(dataCollection));

        when(personRepository.existsById(any()))
                .thenReturn(true);

        when(validationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DataCollectionValidationResponse response =
                service.create(
                        new CreateDataCollectionValidationCommand(
                                dataCollectionUuid,
                                UUID.randomUUID(),
                                ValidationDecision.REJECTED,
                                "Incomplete data",
                                Instant.now()));

        assertEquals(
                ValidationDecision.REJECTED,
                response.decision());

        assertEquals(
                DataCollectionStatus.REJECTED,
                dataCollection.getStatus());

        verify(dataCollectionRepository).save(dataCollection);
        verify(validationRepository).save(any());
    }

    @Test
    void shouldRejectCreationWhenDataCollectionIsNotSubmitted() {

        UUID dataCollectionUuid = UUID.randomUUID();

        DataCollection dataCollection =
                createDataCollection(
                        dataCollectionUuid,
                        DataCollectionStatus.DRAFT);

        when(dataCollectionRepository.findById(any()))
                .thenReturn(Optional.of(dataCollection));

        assertThrows(
                DataCollectionValidationNotAllowedException.class,
                () -> service.create(
                        new CreateDataCollectionValidationCommand(
                                dataCollectionUuid,
                                UUID.randomUUID(),
                                ValidationDecision.VALIDATED,
                                null,
                                Instant.now())));

        verify(personRepository, never()).existsById(any());
        verify(dataCollectionRepository, never()).save(any());
        verify(validationRepository, never()).save(any());
    }

    @Test
    void shouldRejectCreationWhenDataCollectionIsAlreadyValidated() {

        UUID dataCollectionUuid = UUID.randomUUID();

        DataCollection dataCollection =
                createDataCollection(
                        dataCollectionUuid,
                        DataCollectionStatus.VALIDATED);

        when(dataCollectionRepository.findById(any()))
                .thenReturn(Optional.of(dataCollection));

        assertThrows(
                DataCollectionValidationNotAllowedException.class,
                () -> service.create(
                        new CreateDataCollectionValidationCommand(
                                dataCollectionUuid,
                                UUID.randomUUID(),
                                ValidationDecision.VALIDATED,
                                null,
                                Instant.now())));

        verify(personRepository, never()).existsById(any());
        verify(dataCollectionRepository, never()).save(any());
        verify(validationRepository, never()).save(any());
    }

    @Test
    void shouldRejectCreationWhenDataCollectionIsAlreadyRejected() {

        UUID dataCollectionUuid = UUID.randomUUID();

        DataCollection dataCollection =
                createDataCollection(
                        dataCollectionUuid,
                        DataCollectionStatus.REJECTED);

        when(dataCollectionRepository.findById(any()))
                .thenReturn(Optional.of(dataCollection));

        assertThrows(
                DataCollectionValidationNotAllowedException.class,
                () -> service.create(
                        new CreateDataCollectionValidationCommand(
                                dataCollectionUuid,
                                UUID.randomUUID(),
                                ValidationDecision.VALIDATED,
                                null,
                                Instant.now())));

        verify(personRepository, never()).existsById(any());
        verify(dataCollectionRepository, never()).save(any());
        verify(validationRepository, never()).save(any());
    }

    @Test
    void shouldRejectCreationWhenDataCollectionDoesNotExist() {

        UUID dataCollectionUuid = UUID.randomUUID();

        when(dataCollectionRepository.findById(any()))
                .thenReturn(Optional.empty());

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

        DataCollection dataCollection =
                createDataCollection(
                        UUID.randomUUID(),
                        DataCollectionStatus.SUBMITTED);

        when(dataCollectionRepository.findById(any()))
                .thenReturn(Optional.of(dataCollection));

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

    private DataCollection createDataCollection(
            UUID id,
            DataCollectionStatus status) {

        return DataCollection.restore(
                DataCollectionId.of(id),
                CampaignId.generate(),
                CountryId.generate(),
                QuestionnaireId.generate(),
                OrganizationId.generate(),
                OrganizationId.generate(),
                status);
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
