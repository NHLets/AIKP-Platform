package org.afdb.aikp.modules.country.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Command used to create a Country.
 */
public record CreateCountryCommand(

        @NotBlank
        @Pattern(regexp = "[A-Z]{2}")
        String iso2Code,

        @NotBlank
        @Pattern(regexp = "[A-Z]{3}")
        String iso3Code,

        @NotBlank
        @Pattern(regexp = "\\d{3}")
        String numericCode,

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 200)
        String officialName

) {
}