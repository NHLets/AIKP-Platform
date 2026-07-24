package org.afdb.aikp.modules.country.presentation.mapper;

import org.afdb.aikp.modules.country.application.command.CreateCountryCommand;
import org.afdb.aikp.modules.country.application.command.UpdateCountryCommand;
import org.afdb.aikp.modules.country.presentation.request.CreateCountryRequest;
import org.afdb.aikp.modules.country.presentation.request.UpdateCountryRequest;

import java.util.UUID;

/**
 * Maps REST requests to application commands.
 */
public final class CountryRestMapper {

    private CountryRestMapper() {
    }

    public static CreateCountryCommand toCommand(CreateCountryRequest request) {

        return new CreateCountryCommand(
                request.iso2Code(),
                request.iso3Code(),
                request.numericCode(),
                request.name(),
                request.officialName()
        );
    }

    public static UpdateCountryCommand toCommand(
            UUID id,
            UpdateCountryRequest request) {

        return new UpdateCountryCommand(
                id,
                request.iso2Code(),
                request.iso3Code(),
                request.numericCode(),
                request.name(),
                request.officialName()
        );
    }

}