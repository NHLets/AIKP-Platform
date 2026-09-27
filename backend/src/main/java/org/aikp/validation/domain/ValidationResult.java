package org.aikp.validation.domain;

public record ValidationResult(
    boolean valid,
    String message,
    ValidationSeverity severity
){}