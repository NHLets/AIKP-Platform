package org.afdb.aikp.modules.quality.domain.service;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

public final class UnitNormalizationService {

    private static final Map<String, Map<String, BigDecimal>> FACTORS = Map.of(
            "GWH", Map.of("MWH", BigDecimal.valueOf(1000)),
            "MWH", Map.of("GWH", BigDecimal.valueOf(0.001))
    );

    public boolean canConvert(String sourceUnit, String targetUnit) {
        if (sourceUnit == null || targetUnit == null) {
            return false;
        }
        String source = normalize(sourceUnit);
        String target = normalize(targetUnit);
        return source.equals(target)
                || FACTORS.getOrDefault(source, Map.of()).containsKey(target);
    }

    public BigDecimal normalize(
            BigDecimal value,
            String sourceUnit,
            String targetUnit) {

        if (value == null) {
            return null;
        }

        String source = normalize(sourceUnit);
        String target = normalize(targetUnit);

        if (source.equals(target)) {
            return value;
        }

        BigDecimal factor = FACTORS
                .getOrDefault(source, Map.of())
                .get(target);

        if (factor == null) {
            throw new IllegalArgumentException(
                    "Unsupported unit conversion: "
                            + sourceUnit + " -> " + targetUnit);
        }

        return value.multiply(factor);
    }

    private static String normalize(String unit) {
        return unit == null
                ? ""
                : unit.trim().toUpperCase(Locale.ROOT);
    }
}
