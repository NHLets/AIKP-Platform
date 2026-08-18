package org.afdb.aikp.modules.organization.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * HTTP request used to update an organization.
 */
public record UpdateOrganizationRequest(

        @NotBlank
        @Size(max = 50)
        String code,

        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        String type,

        @NotNull
        UUID countryId

) {
}
