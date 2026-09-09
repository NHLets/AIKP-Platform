package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireGroup;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireGroupEntity;
import org.springframework.stereotype.Component;

/**
 * Maps QuestionnaireGroup domain objects to JPA entities and vice versa.
 */
@Component
public class QuestionnaireGroupPersistenceMapper {

    public QuestionnaireGroupEntity toEntity(
            QuestionnaireGroup group) {

        if (group == null) {
            return null;
        }

        QuestionnaireGroupEntity entity =
                new QuestionnaireGroupEntity(
                        group.getId().getValue());

        updateEntity(group, entity);

        return entity;
    }

    public QuestionnaireGroup toDomain(
            QuestionnaireGroupEntity entity) {

        if (entity == null) {
            return null;
        }

        QuestionnaireGroupId parentGroupId =
                entity.getParentGroupId() == null
                        ? null
                        : QuestionnaireGroupId.of(
                                entity.getParentGroupId());

        return QuestionnaireGroup.restore(
                QuestionnaireGroupId.of(entity.getId()),
                QuestionnaireId.of(
                        entity.getQuestionnaireId()),
                parentGroupId,
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getGroupType(),
                entity.getDisplayOrder(),
                entity.isActive()
        );
    }

    public void updateEntity(
            QuestionnaireGroup group,
            QuestionnaireGroupEntity entity) {

        if (group == null) {
            throw new IllegalArgumentException(
                    "QuestionnaireGroup cannot be null.");
        }

        if (entity == null) {
            throw new IllegalArgumentException(
                    "QuestionnaireGroupEntity cannot be null.");
        }

        entity.setQuestionnaireId(
                group.getQuestionnaireId().getValue());

        entity.setParentGroupId(
                group.getParentGroupId() == null
                        ? null
                        : group.getParentGroupId().getValue());

        entity.setCode(group.getCode());
        entity.setName(group.getName());
        entity.setDescription(group.getDescription());
        entity.setGroupType(group.getGroupType());
        entity.setDisplayOrder(group.getDisplayOrder());
        entity.setActive(group.isActive());
    }
}
