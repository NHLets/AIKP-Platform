package org.afdb.aikp.modules.iam.role.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * REST request for updating an IAM Role.
 */
public record UpdateRoleRequest(

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 500)
        String description) {
}
