package org.afdb.aikp.modules.questionnaire.domain.repository;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;

/**
 * Repository abstraction for QuestionnaireGroup aggregates.
 */
public interface QuestionnaireGroupRepository {

    QuestionnaireGroup save(
            QuestionnaireGroup group);

    Optional<QuestionnaireGroup> findById(
            QuestionnaireGroupId id);

    List<QuestionnaireGroup> findByQuestionnaireId(
            QuestionnaireId questionnaireId);

    List<QuestionnaireGroup> findRootGroupsByQuestionnaireId(
            QuestionnaireId questionnaireId);

    List<QuestionnaireGroup> findByQuestionnaireIdAndParentGroupId(
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId parentGroupId);

    boolean existsRootGroupCode(
            QuestionnaireId questionnaireId,
            String code);

    boolean existsChildGroupCode(
            QuestionnaireId questionnaireId,
            QuestionnaireGroupId parentGroupId,
            String code);

    void delete(
            QuestionnaireGroup group);
}
