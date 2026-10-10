package org.afdb.aikp.modules.quality.infrastructure.persistence.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleProvenance;
import org.afdb.aikp.modules.quality.domain.enums.QualityRuleType;
import org.afdb.aikp.modules.quality.domain.enums.QualitySeverity;
import org.afdb.aikp.modules.quality.domain.model.RuleEvaluation;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityRuleEvaluationEntity;

@Component
public class QualityRuleEvaluationPersistenceMapper {

    private final ObjectMapper objectMapper;

    public QualityRuleEvaluationPersistenceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public QualityRuleEvaluationEntity toEntity(
            QualityRunId qualityRunId,
            RuleEvaluation domain) {

        QualityRuleEvaluationEntity entity =
                new QualityRuleEvaluationEntity();

        entity.setId(java.util.UUID.randomUUID());
        entity.setQualityRunId(qualityRunId.getValue());
        entity.setRuleCode(domain.ruleCode());
        entity.setRuleType(domain.ruleType().name());
        entity.setProvenance(domain.provenance().name());
        entity.setSeverity(domain.severity().name());
        entity.setStatus(domain.status().name());
        entity.setTitle(domain.title());
        entity.setMessage(domain.message());
        entity.setExplanation(domain.explanation());
        entity.setRecommendation(domain.recommendation());
        entity.setReferenceYear(domain.referenceYear());

        entity.setAffectedVariables(
                objectMapper.valueToTree(
                        domain.affectedVariableCodes()));

        entity.setEvidence(
                objectMapper.valueToTree(
                        domain.evidence()));

        return entity;
    }

    public RuleEvaluation toDomain(
            QualityRuleEvaluationEntity entity) {

        List<String> affectedVariables =
                objectMapper.convertValue(
                        entity.getAffectedVariables(),
                        new TypeReference<List<String>>() {});

        Map<String, Object> evidence =
                objectMapper.convertValue(
                        entity.getEvidence(),
                        new TypeReference<Map<String, Object>>() {});

        return new RuleEvaluation(
                entity.getRuleCode(),
                QualityRuleType.valueOf(entity.getRuleType()),
                QualityRuleProvenance.valueOf(entity.getProvenance()),
                QualitySeverity.valueOf(entity.getSeverity()),
                QualityFindingStatus.valueOf(entity.getStatus()),
                entity.getTitle(),
                entity.getMessage(),
                entity.getExplanation(),
                entity.getRecommendation(),
                entity.getReferenceYear(),
                affectedVariables,
                evidence);
    }
}
