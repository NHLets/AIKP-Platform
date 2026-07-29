package org.afdb.aikp.modules.iam.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateUserCommand(

        @NotNull
        UUID id,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 2, max = 150)
        String fullName
) {
}