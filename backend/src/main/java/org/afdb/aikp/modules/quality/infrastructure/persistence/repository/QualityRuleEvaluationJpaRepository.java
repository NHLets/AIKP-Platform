package org.afdb.aikp.modules.quality.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityRuleEvaluationEntity;

public interface QualityRuleEvaluationJpaRepository
        extends JpaRepository<QualityRuleEvaluationEntity, UUID> {

    List<QualityRuleEvaluationEntity>
    findByQualityRunIdOrderByRuleCodeAsc(UUID qualityRunId);

    void deleteByQualityRunId(UUID qualityRunId);
}
