package org.afdb.aikp.modules.person.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * HTTP request used to update a person's official information.
 */
public record UpdatePersonRequest(

        @NotBlank
        @Size(max = 255)
        String fullName,

        @NotNull
        UUID organizationId

) {
}
