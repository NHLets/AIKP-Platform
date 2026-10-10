package org.afdb.aikp.modules.quality.application.dto;

import java.util.List;
import java.util.Map;

import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;

public record QualityFindingResponse(
        String id,
        String qualityRunId,
        String ruleCode,
        QualityRuleType ruleType,
        QualityRuleProvenance provenance,
        QualitySeverity severity,
        QualityFindingStatus status,
        String title,
        String message,
        String explanation,
        String recommendation,
        Integer referenceYear,
        List<String> affectedVariableCodes,
        Map<String, Object> evidence) {
}
