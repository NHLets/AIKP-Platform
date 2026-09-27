package org.afdb.aikp.modules.validation.application.dto;

public record SeverityDistributionDto(
    String severity,
    long count
) {
}
