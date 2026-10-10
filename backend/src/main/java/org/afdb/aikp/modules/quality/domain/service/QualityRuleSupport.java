package org.afdb.aikp.modules.quality.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;

public final class QualityRuleSupport {

    public static final BigDecimal DEFAULT_RELATIVE_TOLERANCE =
            new BigDecimal("0.005");

    public static final BigDecimal DEFAULT_ABSOLUTE_TOLERANCE =
            new BigDecimal("0.01");

    private QualityRuleSupport() {
    }

    public static boolean approximatelyEqual(
            BigDecimal observed,
            BigDecimal expected) {

        BigDecimal difference = observed.subtract(expected).abs();
        BigDecimal scale = observed.abs().max(expected.abs());

        BigDecimal relativeLimit =
                scale.multiply(DEFAULT_RELATIVE_TOLERANCE);

        BigDecimal limit = relativeLimit.max(
                DEFAULT_ABSOLUTE_TOLERANCE);

        return difference.compareTo(limit) <= 0;
    }

    public static BigDecimal relativeDifference(
            BigDecimal observed,
            BigDecimal expected) {

        BigDecimal denominator = observed.abs().max(expected.abs());

        if (denominator.signum() == 0) {
            return BigDecimal.ZERO;
        }

        return observed.subtract(expected).abs()
                .divide(denominator, 8, RoundingMode.HALF_UP);
    }

    public static RuleEvaluation missing(
            String code,
            QualityRuleType type,
            QualityRuleProvenance provenance,
            QualitySeverity severity,
            String title,
            List<String> variables) {

        return RuleEvaluation.notEvaluable(
                code, type, provenance, severity, title,
                "Required data is missing or cannot be evaluated.",
                null, variables, Map.of());
    }
}
