package org.afdb.aikp.modules.questionnaire.domain.valueobject;

import org.afdb.aikp.shared.domain.Identifier;

import java.util.UUID;

/**
 * Strongly typed identifier for Questionnaire.
 */
public final class QuestionnaireId extends Identifier<UUID> {

    private QuestionnaireId(UUID value) {
        super(value);
    }

    public static QuestionnaireId of(UUID value) {
        return new QuestionnaireId(value);
    }

    public static QuestionnaireId generate() {
        return new QuestionnaireId(UUID.randomUUID());
    }
}