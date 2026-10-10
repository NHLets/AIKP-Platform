package org.afdb.aikp.modules.quality.domain.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.enums.QualityLevel;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;

public final class QualityAssessment {

    private final UUID id;
    private final DataCollectionId dataCollectionId;
    private final QualityRunId qualityRunId;
    private final QualityLevel overallLevel;
    private final boolean analysisComplete;
    private final List<QualityDimensionAssessment> dimensions;
    private final List<QualityFinding> significantFindings;
    private final String summary;
    private final OffsetDateTime generatedAt;

    private QualityAssessment(
            UUID id,
            DataCollectionId dataCollectionId,
            QualityRunId qualityRunId,
            QualityLevel overallLevel,
            boolean analysisComplete,
            List<QualityDimensionAssessment> dimensions,
            List<QualityFinding> significantFindings,
            String summary,
            OffsetDateTime generatedAt) {

        this.id = id;
        this.dataCollectionId = dataCollectionId;
        this.qualityRunId = qualityRunId;
        this.overallLevel = overallLevel;
        this.analysisComplete = analysisComplete;
        this.dimensions = List.copyOf(dimensions);
        this.significantFindings = List.copyOf(significantFindings);
        this.summary = summary;
        this.generatedAt = generatedAt;
    }

    public static QualityAssessment create(
            DataCollectionId dataCollectionId,
            QualityRunId qualityRunId,
            QualityLevel overallLevel,
            boolean analysisComplete,
            List<QualityDimensionAssessment> dimensions,
            List<QualityFinding> significantFindings,
            String summary) {

        return new QualityAssessment(
                UUID.randomUUID(),
                dataCollectionId,
                qualityRunId,
                overallLevel,
                analysisComplete,
                dimensions,
                significantFindings,
                summary,
                OffsetDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public DataCollectionId getDataCollectionId() {
        return dataCollectionId;
    }

    public QualityRunId getQualityRunId() {
        return qualityRunId;
    }

    public QualityLevel getOverallLevel() {
        return overallLevel;
    }

    public boolean isAnalysisComplete() {
        return analysisComplete;
    }

    public List<QualityDimensionAssessment> getDimensions() {
        return dimensions;
    }

    public List<QualityFinding> getSignificantFindings() {
        return significantFindings;
    }

    public String getSummary() {
        return summary;
    }

    public OffsetDateTime getGeneratedAt() {
        return generatedAt;
    }
}
