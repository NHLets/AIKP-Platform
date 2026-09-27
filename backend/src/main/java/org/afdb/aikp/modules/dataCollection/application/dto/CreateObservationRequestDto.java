package org.afdb.aikp.modules.dataCollection.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateObservationRequestDto(

    @NotNull UUID dataCollectionId,

    @NotNull UUID variableId,

    @NotNull UUID organizationId,

    @NotBlank String value

) {}
