package org.aikp.validation.domain;

public record ValidationIssue(

    String questionnaire,

    String variableCode,

    String ruleCode,

    ValidationSeverity severity,

    String message

){}