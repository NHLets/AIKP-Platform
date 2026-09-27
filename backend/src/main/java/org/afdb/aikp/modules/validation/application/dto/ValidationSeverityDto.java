package org.afdb.aikp.modules.validation.application.dto;

public record ValidationSeverityDto(
    String severity,
    long total
) {
}
