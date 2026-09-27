package org.aikp.validation.domain;

import java.util.List;

public record ValidationReport(

    String questionnaire,

    List<ValidationIssue> issues,

    ValidationSeverity overallSeverity

){}