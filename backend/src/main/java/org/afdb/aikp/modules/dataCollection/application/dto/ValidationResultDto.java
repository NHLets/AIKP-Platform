package org.afdb.aikp.modules.dataCollection.application.dto;

public record ValidationResultDto(
    boolean valid,
    String rule,
    String message
) {}
