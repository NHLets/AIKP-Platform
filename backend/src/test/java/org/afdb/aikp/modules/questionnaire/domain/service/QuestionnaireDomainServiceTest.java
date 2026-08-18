package org.afdb.aikp.modules.questionnaire.domain.service;

import org.afdb.aikp.modules.questionnaire.domain.exception.QuestionnaireCodeAlreadyExistsException;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;
import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class QuestionnaireDomainServiceTest {

    private QuestionnaireRepository repository;

    private QuestionnaireDomainService service;

    @BeforeEach
    void setUp() {

        repository = mock(QuestionnaireRepository.class);

        service = new QuestionnaireDomainServiceImpl(repository);
    }

    @Test
    void shouldAllowCreationWhenCodeDoesNotExist() {

        QuestionnaireCode code =
                QuestionnaireCode.of("PW_NEW");

        when(repository.existsByCode(code))
                .thenReturn(false);

        assertThatCode(() ->
                service.validateCreation(code))
                .doesNotThrowAnyException();

        verify(repository)
                .existsByCode(code);
    }

    @Test
    void shouldRejectCreationWhenCodeAlreadyExists() {

        QuestionnaireCode code =
                QuestionnaireCode.of("PW_A");

        when(repository.existsByCode(code))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.validateCreation(code))
                .isInstanceOf(
                        QuestionnaireCodeAlreadyExistsException.class);

        verify(repository)
                .existsByCode(code);
    }

    @Test
    void shouldAllowUpdateWhenCodeDoesNotExist() {

        QuestionnaireId questionnaireId =
                QuestionnaireId.of(UUID.randomUUID());

        QuestionnaireCode code =
                QuestionnaireCode.of("PW_NEW");

        when(repository.findByCode(code))
                .thenReturn(Optional.empty());

        assertThatCode(() ->
                service.validateUpdate(
                        questionnaireId,
                        code))
                .doesNotThrowAnyException();

        verify(repository)
                .findByCode(code);
    }

    @Test
    void shouldAllowUpdateWhenCodeBelongsToSameQuestionnaire() {

        Questionnaire questionnaire =
                createQuestionnaire("PW_A");

        QuestionnaireId questionnaireId =
                questionnaire.getId();

        QuestionnaireCode code =
                questionnaire.getCode();

        when(repository.findByCode(code))
                .thenReturn(Optional.of(questionnaire));

        assertThatCode(() ->
                service.validateUpdate(
                        questionnaireId,
                        code))
                .doesNotThrowAnyException();

        verify(repository)
                .findByCode(code);
    }

    @Test
    void shouldRejectUpdateWhenCodeBelongsToAnotherQuestionnaire() {

        Questionnaire currentQuestionnaire =
                createQuestionnaire("PW_A");

        Questionnaire otherQuestionnaire =
                createQuestionnaire("PW_B");

        QuestionnaireId currentId =
                currentQuestionnaire.getId();

        QuestionnaireCode otherCode =
                otherQuestionnaire.getCode();

        when(repository.findByCode(otherCode))
                .thenReturn(Optional.of(otherQuestionnaire));

        assertThatThrownBy(() ->
                service.validateUpdate(
                        currentId,
                        otherCode))
                .isInstanceOf(
                        QuestionnaireCodeAlreadyExistsException.class);

        verify(repository)
                .findByCode(otherCode);
    }

    @Test
    void shouldRejectNullCodeDuringCreation() {

        assertThatThrownBy(() ->
                service.validateCreation(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(repository);
    }

    @Test
    void shouldRejectNullArgumentsDuringUpdate() {

        QuestionnaireCode code =
                QuestionnaireCode.of("PW_A");

        assertThatThrownBy(() ->
                service.validateUpdate(null, code))
                .isInstanceOf(NullPointerException.class);

        QuestionnaireId id =
                QuestionnaireId.of(UUID.randomUUID());

        assertThatThrownBy(() ->
                service.validateUpdate(id, null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(repository);
    }

    private Questionnaire createQuestionnaire(String code) {

        return Questionnaire.create(
                QuestionnaireCode.of(code),
                QuestionnaireName.of(
                        "Questionnaire " + code),
                QuestionnaireDescription.of(
                        "Test questionnaire"),
                QuestionnaireVersion.of("1.0"),
                DefaultLanguage.of("en"),
                RenderType.FORM);
    }
}
