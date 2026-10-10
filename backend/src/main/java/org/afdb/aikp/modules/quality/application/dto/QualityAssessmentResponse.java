package org.afdb.aikp.modules.quality.application.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.quality.domain.enums.QualityLevel;

public record QualityAssessmentResponse(
        UUID dataCollectionId,
        UUID qualityRunId,
        QualityLevel overallLevel,
        boolean analysisComplete,
        String summary,
        OffsetDateTime generatedAt,
        List<QualityDimensionAssessmentResponse> dimensions,
        List<QualityFindingResponse> significantFindings) {
}
