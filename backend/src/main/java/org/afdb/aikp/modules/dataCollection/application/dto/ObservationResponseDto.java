package org.afdb.aikp.modules.dataCollection.application.dto;

import java.util.UUID;

public record ObservationResponseDto(

    UUID id,

    UUID variableId,

    UUID organizationId,

    String value,

    String status

) {}
