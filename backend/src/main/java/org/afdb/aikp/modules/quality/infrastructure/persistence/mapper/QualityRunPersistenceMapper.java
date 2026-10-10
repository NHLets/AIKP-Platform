package org.afdb.aikp.modules.quality.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunTrigger;
import org.afdb.aikp.modules.quality.domain.model.QualityRun;
import org.afdb.aikp.modules.quality.domain.valueobject.QualityRunId;
import org.afdb.aikp.modules.quality.infrastructure.persistence.entity.QualityRunEntity;

@Component
public class QualityRunPersistenceMapper {

    public QualityRunEntity toEntity(QualityRun domain) {
        QualityRunEntity entity = new QualityRunEntity();

        entity.setId(domain.getId().getValue());
        entity.setDataCollectionId(
                domain.getDataCollectionId().getValue());
        entity.setTrigger(domain.getTrigger().name());
        entity.setStatus(domain.getStatus().name());
        entity.setStartedAt(domain.getStartedAt());
        entity.setCompletedAt(domain.getCompletedAt());
        entity.setRulesEvaluated(domain.getRulesEvaluated());
        entity.setRulesPassed(domain.getRulesPassed());
        entity.setRulesFailed(domain.getRulesFailed());
        entity.setRulesNotEvaluable(domain.getRulesNotEvaluable());
        entity.setRulesNotApplicable(domain.getRulesNotApplicable());
        entity.setRulesErrored(domain.getRulesErrored());

        return entity;
    }

    public QualityRun toDomain(QualityRunEntity entity) {
        return QualityRun.reconstitute(
                QualityRunId.of(entity.getId()),
                DataCollectionId.of(entity.getDataCollectionId()),
                QualityRunTrigger.valueOf(entity.getTrigger()),
                entity.getStartedAt(),
                QualityRunStatus.valueOf(entity.getStatus()),
                entity.getCompletedAt(),
                entity.getRulesEvaluated(),
                entity.getRulesPassed(),
                entity.getRulesFailed(),
                entity.getRulesNotEvaluable(),
                entity.getRulesNotApplicable(),
                entity.getRulesErrored(),
                java.util.List.of());
    }
}
