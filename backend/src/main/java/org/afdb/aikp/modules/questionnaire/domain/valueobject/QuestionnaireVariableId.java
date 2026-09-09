package org.afdb.aikp.modules.questionnaire.domain.valueobject;

import java.util.UUID;

import org.afdb.aikp.shared.domain.Identifier;

/**
 * Strongly typed identifier for QuestionnaireVariable.
 */
public final class QuestionnaireVariableId
        extends Identifier<UUID> {

    private QuestionnaireVariableId(UUID value) {
        super(value);
    }

    public static QuestionnaireVariableId of(UUID value) {
        return new QuestionnaireVariableId(value);
    }

    public static QuestionnaireVariableId generate() {
        return new QuestionnaireVariableId(
                UUID.randomUUID()
        );
    }
}
