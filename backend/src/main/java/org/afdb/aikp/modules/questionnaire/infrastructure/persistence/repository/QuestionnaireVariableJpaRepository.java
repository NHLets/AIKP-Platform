package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireVariableEntity;

/**
 * Spring Data repository for questionnaire variables.
 */
public interface QuestionnaireVariableJpaRepository
        extends JpaRepository<QuestionnaireVariableEntity, UUID> {

    List<QuestionnaireVariableEntity>
            findByQuestionnaireIdOrderByDisplayOrderAsc(
                    UUID questionnaireId);

    List<QuestionnaireVariableEntity>
            findByQuestionnaireGroupIdOrderByDisplayOrderAsc(
                    UUID questionnaireGroupId);

    boolean existsByQuestionnaireIdAndSeriesCode(
            UUID questionnaireId,
            String seriesCode);
}
