package org.afdb.aikp.modules.questionnaire.domain.repository;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;

/**
 * Repository abstraction for QuestionnaireVariable aggregates.
 */
public interface QuestionnaireVariableRepository {

    QuestionnaireVariable save(
            QuestionnaireVariable variable);

    Optional<QuestionnaireVariable> findById(
            QuestionnaireVariableId id);

    List<QuestionnaireVariable> findByQuestionnaireId(
            QuestionnaireId questionnaireId);

    List<QuestionnaireVariable> findByQuestionnaireGroupId(
            QuestionnaireGroupId questionnaireGroupId);

    boolean existsByQuestionnaireIdAndSeriesCode(
            QuestionnaireId questionnaireId,
            String seriesCode);

    void delete(
            QuestionnaireVariable variable);
}
