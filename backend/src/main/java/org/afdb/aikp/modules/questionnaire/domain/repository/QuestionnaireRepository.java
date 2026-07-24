package org.afdb.aikp.modules.questionnaire.domain.repository;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for Questionnaire aggregates.
 */
public interface QuestionnaireRepository {

    /**
     * Persists a questionnaire.
     *
     * @param questionnaire aggregate to persist
     * @return persisted aggregate
     */
    Questionnaire save(Questionnaire questionnaire);

    /**
     * Finds a questionnaire by its identifier.
     */
    Optional<Questionnaire> findById(QuestionnaireId id);

    /**
     * Finds a questionnaire by its business code.
     */
    Optional<Questionnaire> findByCode(QuestionnaireCode code);

    /**
     * Returns all questionnaires.
     */
    List<Questionnaire> findAll();

    /**
     * Returns all active questionnaires.
     */
    List<Questionnaire> findActive();

    /**
     * Returns all questionnaires having the given status.
     */
    List<Questionnaire> findByStatus(QuestionnaireStatus status);

    /**
     * Checks whether a questionnaire exists.
     */
    boolean existsById(QuestionnaireId id);

    /**
     * Checks whether a questionnaire code already exists.
     */
    boolean existsByCode(QuestionnaireCode code);

    /**
     * Deletes a questionnaire.
     */
    void delete(Questionnaire questionnaire);

}