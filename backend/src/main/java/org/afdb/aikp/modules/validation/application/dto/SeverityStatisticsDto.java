package org.afdb.aikp.modules.validation.application.dto;

public record SeverityStatisticsDto(
    String severity,
    long count
) {
}
