package org.afdb.aikp.modules.questionnaire.application.dto;

import java.util.UUID;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireGroupType;

public record QuestionnaireGroupResponse(
        UUID id,
        UUID questionnaireId,
        UUID parentGroupId,
        String code,
        String name,
        String description,
        QuestionnaireGroupType groupType,
        int displayOrder,
        boolean active) {
}
