package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireVariableEntity;
import org.springframework.stereotype.Component;

/**
 * Maps QuestionnaireVariable domain objects to JPA entities and vice versa.
 */
@Component
public class QuestionnaireVariablePersistenceMapper {

    public QuestionnaireVariableEntity toEntity(
            QuestionnaireVariable variable) {

        if (variable == null) {
            return null;
        }

        QuestionnaireVariableEntity entity =
                new QuestionnaireVariableEntity(
                        variable.getId().getValue());

        updateEntity(variable, entity);

        return entity;
    }

    public QuestionnaireVariable toDomain(
            QuestionnaireVariableEntity entity) {

        if (entity == null) {
            return null;
        }

        QuestionnaireGroupId groupId =
                entity.getQuestionnaireGroupId() == null
                        ? null
                        : QuestionnaireGroupId.of(
                                entity.getQuestionnaireGroupId());

        return QuestionnaireVariable.restore(
                QuestionnaireVariableId.of(entity.getId()),
                QuestionnaireId.of(
                        entity.getQuestionnaireId()),
                groupId,
                entity.getSeriesCode(),
                entity.getName(),
                entity.getDefinition(),
                entity.getDataType(),
                entity.getUnit(),
                entity.isRequired(),
                entity.getDisplayOrder(),
                entity.isActive()
        );
    }

    public void updateEntity(
            QuestionnaireVariable variable,
            QuestionnaireVariableEntity entity) {

        if (variable == null) {
            throw new IllegalArgumentException(
                    "QuestionnaireVariable cannot be null.");
        }

        if (entity == null) {
            throw new IllegalArgumentException(
                    "QuestionnaireVariableEntity cannot be null.");
        }

        entity.setQuestionnaireId(
                variable.getQuestionnaireId().getValue());

        entity.setQuestionnaireGroupId(
                variable.getQuestionnaireGroupId() == null
                        ? null
                        : variable.getQuestionnaireGroupId()
                                .getValue());

        entity.setSeriesCode(
                variable.getSeriesCode());

        entity.setName(variable.getName());

        entity.setDefinition(
                variable.getDefinition());

        entity.setDataType(
                variable.getDataType());

        entity.setUnit(variable.getUnit());

        entity.setRequired(
                variable.isRequired());

        entity.setDisplayOrder(
                variable.getDisplayOrder());

        entity.setActive(
                variable.isActive());
    }
}
