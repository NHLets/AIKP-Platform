package org.afdb.aikp.modules.questionnaire.application.dto;

public record UpdateQuestionnaireGroupRequest(
        String name,
        String description,
        int displayOrder) {
}
