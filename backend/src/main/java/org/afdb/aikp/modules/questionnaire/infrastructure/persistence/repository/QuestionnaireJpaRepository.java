package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireStatus;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Questionnaire entities.
 */
public interface QuestionnaireJpaRepository
        extends JpaRepository<QuestionnaireEntity, UUID> {

    /**
     * Finds a questionnaire by its business code.
     */
    Optional<QuestionnaireEntity> findByCode(String code);

    /**
     * Returns all active questionnaires.
     */
    List<QuestionnaireEntity> findByActiveTrue();

    /**
     * Returns all questionnaires having the given status.
     */
    List<QuestionnaireEntity> findByStatus(QuestionnaireStatus status);

    /**
     * Checks whether a questionnaire exists by its business code.
     */
    boolean existsByCode(String code);

}