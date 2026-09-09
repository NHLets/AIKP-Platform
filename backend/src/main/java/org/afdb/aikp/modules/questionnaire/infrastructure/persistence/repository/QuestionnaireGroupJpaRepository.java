package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireGroupEntity;

/**
 * Spring Data repository for questionnaire groups.
 */
public interface QuestionnaireGroupJpaRepository
        extends JpaRepository<QuestionnaireGroupEntity, UUID> {

    List<QuestionnaireGroupEntity>
            findByQuestionnaireIdOrderByDisplayOrderAsc(
                    UUID questionnaireId);

    List<QuestionnaireGroupEntity>
            findByQuestionnaireIdAndParentGroupIdOrderByDisplayOrderAsc(
                    UUID questionnaireId,
                    UUID parentGroupId);

    List<QuestionnaireGroupEntity>
            findByQuestionnaireIdAndParentGroupIdIsNullOrderByDisplayOrderAsc(
                    UUID questionnaireId);

    boolean existsByQuestionnaireIdAndCodeAndParentGroupIdIsNull(
            UUID questionnaireId,
            String code);

    boolean existsByQuestionnaireIdAndParentGroupIdAndCode(
            UUID questionnaireId,
            UUID parentGroupId,
            String code);
}
