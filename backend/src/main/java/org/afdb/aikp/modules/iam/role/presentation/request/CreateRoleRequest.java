package org.afdb.aikp.modules.iam.role.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * REST request for creating an IAM Role.
 */
public record CreateRoleRequest(

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 500)
        String description,

        @NotNull
        Boolean system) {
}
