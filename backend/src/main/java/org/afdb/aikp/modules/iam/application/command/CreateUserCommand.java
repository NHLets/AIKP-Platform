package org.afdb.aikp.modules.iam.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserCommand(

        @NotBlank
        @Size(min = 3, max = 50)
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 2, max = 150)
        String fullName,

        @NotBlank
        @Size(min = 8, max = 128)
        String password
) {
}