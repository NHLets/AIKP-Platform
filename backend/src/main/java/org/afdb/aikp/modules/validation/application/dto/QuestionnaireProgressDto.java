package org.afdb.aikp.modules.validation.application.dto;

public record QuestionnaireProgressDto(
    String questionnaireCode,
    String questionnaireName,
    long completed,
    long total,
    double completionRate
) {
}
