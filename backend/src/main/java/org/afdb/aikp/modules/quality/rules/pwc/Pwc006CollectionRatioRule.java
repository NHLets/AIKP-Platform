package org.afdb.aikp.modules.quality.rules.pwc;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.service.QualityEvaluationContext;
import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public final class Pwc006CollectionRatioRule implements QualityRule {

    @Override
    public String code() {
        return "PWC-006";
    }

    @Override
    public QualityRuleType type() {
        return QualityRuleType.LOGICAL;
    }

    @Override
    public QualityRuleProvenance provenance() {
        return QualityRuleProvenance.PROPOSED;
    }

    @Override
    public QualitySeverity severity() {
        return QualitySeverity.MAJOR;
    }

    @Override
    public boolean appliesTo(QualityEvaluationContext context) {
        return context.variable("B061").isPresent()
                && context.variable("B069").isPresent();
    }

    @Override
    public RuleEvaluation evaluate(QualityEvaluationContext context) {
        if (context.referenceYears().isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Collection ratio cannot be evaluated.",
                    "No reference year is available for B061 and B069.",
                    null,
                    List.of("B061", "B069"),
                    Map.of()
            );
        }

        int year = context.latestReferenceYear();

        var b061 = context.numericValue("B061", year);
        var b069 = context.numericValue("B069", year);

        if (b061.isEmpty() || b069.isEmpty()) {
            return RuleEvaluation.notEvaluable(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "Collection ratio cannot be evaluated.",
                    "B061 and B069 must both be available for the evaluated reference year.",
                    year,
                    List.of("B061", "B069"),
                    Map.of()
            );
        }

        BigDecimal collected = b061.get();
        BigDecimal billed = b069.get();

        Map<String, Object> evidence = Map.of(
                "collectedAmountB061", collected,
                "billedAmountB069", billed
        );

        if (collected.compareTo(billed) <= 0) {
            return RuleEvaluation.passed(
                    code(),
                    type(),
                    provenance(),
                    severity(),
                    "B061 does not exceed B069.",
                    year,
                    List.of("B061", "B069"),
                    evidence
            );
        }

        return RuleEvaluation.failed(
                code(),
                type(),
                provenance(),
                severity(),
                "B061 exceeds B069.",
                "The reported collected amount is greater than the reported billed amount.",
                "The collected amount should not exceed the billed amount under the reported collection measure.",
                "Review B061 and B069 and verify the underlying billing and collection data.",
                year,
                List.of("B061", "B069"),
                evidence
        );
    }
}
