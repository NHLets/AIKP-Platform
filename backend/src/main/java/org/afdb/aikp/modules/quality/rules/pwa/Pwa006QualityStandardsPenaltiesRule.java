package org.afdb.aikp.modules.quality.rules.pwa;

import java.util.List;
import java.util.Map;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.springframework.stereotype.Component;

@Component
public final class Pwa006QualityStandardsPenaltiesRule implements QualityRule {

    @Override
    public String code() {
        return "PWA-006";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.LOGICAL;
    }

    @Override
    public QualityRuleProvenance provenance() {
        return QualityRuleProvenance.SOURCE_DEFINED;
    }

    @Override
    public QualitySeverity severity() {
        return QualitySeverity.MAJOR;
    }

    @Override
    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("D037").isPresent()
                && context.variable("D038").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {

        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(), type(), provenance(), severity(),
                    "Quality standards and penalties coherence cannot be evaluated.",
                    "No reference year is available for D037 and D038.",
                    null,
                    List.of("D037", "D038"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var d037 = context.booleanValue("D037", year);
        var d038 = context.booleanValue("D038", year);

        if (d037.isEmpty() || d038.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(), type(), provenance(), severity(),
                    "Quality standards and penalties coherence cannot be evaluated.",
                    "D037 and D038 must both be available for the evaluated reference year.",
                    year,
                    List.of("D037", "D038"),
                    Map.of()
            );
        }

        boolean standards = d037.get();
        boolean penalties = d038.get();

        Map<String, Object> evidence = Map.of(
                "d037MinimumQualityStandards", standards,
                "d038PenaltiesForNonCompliance", penalties
        );

        if (!penalties || standards) {
            return RuleEvaluation.passed(
                    code(), type(), provenance(), severity(),
                    "D037 and D038 are semantically coherent.",
                    year,
                    List.of("D037", "D038"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(), type(), provenance(), severity(),
                "D038 indicates penalties although D037 indicates no quality standards.",
                "D038 states that penalties exist for non-compliance with minimum quality standards, while D037 states that no such minimum quality standards are defined.",
                "Penalties for non-compliance with minimum quality standards should be consistent with the existence of those standards.",
                "Review D037 and D038 and verify the regulatory information.",
                year,
                List.of("D037", "D038"),
                evidence
        );
    }
}
