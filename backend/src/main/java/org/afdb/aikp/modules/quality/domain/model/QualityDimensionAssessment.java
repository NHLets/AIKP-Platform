package org.afdb.aikp.modules.quality.domain.model;

import java.util.List;

import org.afdb.aikp.modules.quality.domain.enums.QualityDimension;
import org.afdb.aikp.modules.quality.domain.enums.QualityDimensionStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityLevel;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.afdb.aikp.modules.quality.domain.model.QualityFinding;

public final class QualityDimensionAssessment {

    private final QualityDimension dimension;
    private final QualityLevel level;
    private final QualityDimensionStatus status;
    private final int rulesEvaluated;
    private final int findingsCount;
    private final List<String> affectedRuleCodes;
    private final String explanation;

    private QualityDimensionAssessment(
            QualityDimension dimension,
            QualityLevel level,
            QualityDimensionStatus status,
            int rulesEvaluated,
            int findingsCount,
            List<String> affectedRuleCodes,
            String explanation) {

        this.dimension = dimension;
        this.level = level;
        this.status = status;
        this.rulesEvaluated = rulesEvaluated;
        this.findingsCount = findingsCount;
        this.affectedRuleCodes = List.copyOf(affectedRuleCodes);
        this.explanation = explanation;
    }

    public static QualityDimensionAssessment evaluated(
            QualityDimension dimension,
            QualityLevel level,
            int rulesEvaluated,
            int findingsCount,
            List<String> affectedRuleCodes,
            String explanation) {

        return new QualityDimensionAssessment(
                dimension,
                level,
                QualityDimensionStatus.EVALUATED,
                rulesEvaluated,
                findingsCount,
                affectedRuleCodes,
                explanation);
    }

    public static QualityDimensionAssessment partiallyEvaluated(
            QualityDimension dimension,
            QualityLevel level,
            int rulesEvaluated,
            int findingsCount,
            List<String> affectedRuleCodes,
            String explanation) {

        return new QualityDimensionAssessment(
                dimension,
                level,
                QualityDimensionStatus.PARTIALLY_EVALUATED,
                rulesEvaluated,
                findingsCount,
                affectedRuleCodes,
                explanation);
    }

    public static QualityDimensionAssessment notEvaluable(
            QualityDimension dimension,
            String explanation) {

        return new QualityDimensionAssessment(
                dimension,
                QualityLevel.FAIR,
                QualityDimensionStatus.NOT_EVALUABLE,
                0,
                0,
                List.of(),
                explanation);
    }

    public QualityDimension getDimension() {
        return dimension;
    }

    public QualityLevel getLevel() {
        return level;
    }

    public QualityDimensionStatus getStatus() {
        return status;
    }

    public int getRulesEvaluated() {
        return rulesEvaluated;
    }

    public int getFindingsCount() {
        return findingsCount;
    }

    public List<String> getAffectedRuleCodes() {
        return affectedRuleCodes;
    }

    public String getExplanation() {
        return explanation;
    }
}
