package org.afdb.aikp.modules.validation.application.dto;

public record ValidationStatisticsDto(
    long validated,
    long pending,
    long rejected,
    long notAvailable,
    long total,
    double completionRate
) {
}
