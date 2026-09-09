package org.afdb.aikp.modules.questionnaire.domain.valueobject;

import java.util.UUID;

import org.afdb.aikp.shared.domain.Identifier;

/**
 * Strongly typed identifier for QuestionnaireGroup.
 */
public final class QuestionnaireGroupId
        extends Identifier<UUID> {

    private QuestionnaireGroupId(UUID value) {
        super(value);
    }

    public static QuestionnaireGroupId of(UUID value) {
        return new QuestionnaireGroupId(value);
    }

    public static QuestionnaireGroupId generate() {
        return new QuestionnaireGroupId(UUID.randomUUID());
    }
}
