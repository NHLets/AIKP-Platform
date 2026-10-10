package org.afdb.aikp.modules.quality.domain.repository;

import java.util.List;

import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;

public interface QualityRuleEvaluationRepository {

    void saveAll(QualityRunId qualityRunId, List<RuleEvaluation> evaluations);

    List<RuleEvaluation> findByQualityRunId(QualityRunId qualityRunId);

    void deleteByQualityRunId(QualityRunId qualityRunId);
}
