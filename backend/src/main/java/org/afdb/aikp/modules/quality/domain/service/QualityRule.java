package org.afdb.aikp.modules.quality.domain.service;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;

public interface QualityRule {

    String code();

    QualityRuleType type();

    QualityRuleProvenance provenance();

    QualitySeverity severity();

    boolean appliesTo(QualityEvaluationContext context);

    RuleEvaluation evaluate(QualityEvaluationContext context);
}
