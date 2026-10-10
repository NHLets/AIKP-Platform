package org.afdb.aikp.modules.quality.application.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.afdb.aikp.modules.quality.application.dto.QualityAssessmentResponse;
import org.afdb.aikp.modules.quality.application.dto.QualityDimensionAssessmentResponse;
import org.afdb.aikp.modules.quality.application.dto.QualityFindingResponse;
import org.afdb.aikp.modules.quality.domain.model.QualityAssessment;
import org.afdb.aikp.modules.quality.domain.model.QualityDimensionAssessment;
import org.afdb.aikp.modules.quality.domain.model.QualityFinding;
import org.springframework.stereotype.Component;

@Component
public class QualityAssessmentApplicationMapper {

    public QualityAssessmentResponse toResponse(QualityAssessment assessment) {
        return new QualityAssessmentResponse(
                assessment.getDataCollectionId().getValue(),
                assessment.getQualityRunId().getValue(),
                assessment.getOverallLevel(),
                assessment.isAnalysisComplete(),
                assessment.getSummary(),
                assessment.getGeneratedAt(),
                assessment.getDimensions().stream()
                        .map(this::toDimensionResponse)
                        .toList(),
                assessment.getSignificantFindings().stream()
                        .map(this::toFindingResponse)
                        .toList());
    }

    private QualityDimensionAssessmentResponse toDimensionResponse(
            QualityDimensionAssessment dimension) {

        return new QualityDimensionAssessmentResponse(
                dimension.getDimension(),
                dimension.getLevel(),
                dimension.getStatus(),
                dimension.getRulesEvaluated(),
                dimension.getFindingsCount(),
                List.copyOf(dimension.getAffectedRuleCodes()),
                dimension.getExplanation());
    }

    private QualityFindingResponse toFindingResponse(QualityFinding finding) {
        return new QualityFindingResponse(
                finding.getId().getValue().toString(),
                finding.getQualityRunId().getValue().toString(),
                finding.getRuleCode(),
                finding.getRuleType(),
                finding.getProvenance(),
                finding.getSeverity(),
                finding.getStatus(),
                finding.getTitle(),
                finding.getMessage(),
                finding.getExplanation(),
                finding.getRecommendation(),
                finding.getReferenceYear(),
                List.copyOf(finding.getAffectedVariableCodes()),
                finding.getEvidence());
    }
}
