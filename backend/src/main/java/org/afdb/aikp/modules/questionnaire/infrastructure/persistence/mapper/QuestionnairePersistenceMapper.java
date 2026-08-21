package org.afdb.aikp.modules.questionnaire.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;
import org.afdb.aikp.modules.questionnaire.infrastructure.persistence.entity.QuestionnaireEntity;
import org.springframework.stereotype.Component;

/**
 * Maps Questionnaire domain objects to JPA entities and vice versa.
 *
 * <p>
 * The mapper keeps persistence concerns outside the domain model.
 * </p>
 */
@Component
public class QuestionnairePersistenceMapper {

    /**
     * Converts a domain aggregate into a JPA entity.
     */
    public QuestionnaireEntity toEntity(
            Questionnaire questionnaire) {

        if (questionnaire == null) {
            return null;
        }

        QuestionnaireEntity entity =
                new QuestionnaireEntity(
                        questionnaire.getId().getValue());

        updateEntity(questionnaire, entity);

        return entity;
    }

    /**
     * Reconstructs a domain aggregate from a JPA entity.
     */
    public Questionnaire toDomain(
            QuestionnaireEntity entity) {

        if (entity == null) {
            return null;
        }

        return Questionnaire.restore(
                QuestionnaireId.of(entity.getId()),
                QuestionnaireCode.of(entity.getCode()),
                QuestionnaireName.of(entity.getName()),
                QuestionnaireDescription.of(
                        entity.getDescription()),
                QuestionnaireVersion.of(
                        entity.getQuestionnaireVersion()),
                DefaultLanguage.of(
                        entity.getDefaultLanguage()),
                entity.getStatus(),
                entity.getRenderType(),
                entity.isActive()
        );
    }

    /**
     * Updates an existing JPA entity from a domain aggregate.
     *
     * <p>
     * The identifier and audit fields are deliberately not modified.
     * </p>
     */
    public void updateEntity(
            Questionnaire questionnaire,
            QuestionnaireEntity entity) {

        if (questionnaire == null) {
            throw new IllegalArgumentException(
                    "Questionnaire cannot be null.");
        }

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Questionnaire entity cannot be null.");
        }

        entity.setCode(
                questionnaire.getCode().getValue());

        entity.setName(
                questionnaire.getName().getValue());

        entity.setDescription(
                questionnaire.getDescription().getValue());

        entity.setQuestionnaireVersion(
                questionnaire.getQuestionnaireVersion()
                        .getValue());

        entity.setDefaultLanguage(
                questionnaire.getDefaultLanguage()
                        .getValue());

        entity.setStatus(
                questionnaire.getStatus());

        entity.setRenderType(
                questionnaire.getRenderType());

        entity.setActive(
                questionnaire.isActive());
    }
}