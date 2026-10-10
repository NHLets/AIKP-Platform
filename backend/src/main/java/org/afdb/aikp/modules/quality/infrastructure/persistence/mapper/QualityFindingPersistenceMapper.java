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
import org.afdb.aikp.modules.quality.domain.model.QualityFinding;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityFindingId;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityFindingEntity;

@Component
public class QualityFindingPersistenceMapper {

    private final ObjectMapper objectMapper;

    public QualityFindingPersistenceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public QualityFindingEntity toEntity(QualityFinding domain) {
        QualityFindingEntity entity = new QualityFindingEntity();

        entity.setId(domain.getId().getValue());
        entity.setQualityRunId(domain.getQualityRunId().getValue());
        entity.setRuleCode(domain.getRuleCode());
        entity.setRuleType(domain.getRuleType().name());
        entity.setProvenance(domain.getProvenance().name());
        entity.setSeverity(domain.getSeverity().name());
        entity.setStatus(domain.getStatus().name());
        entity.setTitle(domain.getTitle());
        entity.setMessage(domain.getMessage());
        entity.setExplanation(domain.getExplanation());
        entity.setRecommendation(domain.getRecommendation());
        entity.setReferenceYear(domain.getReferenceYear());

        entity.setAffectedVariables(
                objectMapper.valueToTree(
                        domain.getAffectedVariableCodes()));

        entity.setEvidence(
                objectMapper.valueToTree(
                        domain.getEvidence()));

        return entity;
    }

    public QualityFinding toDomain(QualityFindingEntity entity) {
        List<String> affectedVariables =
                objectMapper.convertValue(
                        entity.getAffectedVariables(),
                        new TypeReference<List<String>>() {});

        Map<String, Object> evidence =
                objectMapper.convertValue(
                        entity.getEvidence(),
                        new TypeReference<Map<String, Object>>() {});

        return QualityFinding.reconstitute(
                QualityFindingId.of(entity.getId()),
                QualityRunId.of(entity.getQualityRunId()),
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
