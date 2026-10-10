package org.afdb.aikp.modules.quality.application.dto;

import java.util.List;

import org.afdb.aikp.modules.quality.domain.enums.QualityDimension;
import org.afdb.aikp.modules.quality.domain.enums.QualityDimensionStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityLevel;

public record QualityDimensionAssessmentResponse(
        QualityDimension dimension,
        QualityLevel level,
        QualityDimensionStatus status,
        int rulesEvaluated,
        int findingsCount,
        List<String> affectedRuleCodes,
        String explanation) {
}
