package org.afdb.aikp.modules.country.application.mapper;

import org.afdb.aikp.modules.country.application.response.CountryResponse;
import org.afdb.aikp.modules.country.application.response.CountrySummary;
import org.afdb.aikp.modules.country.domain.model.Country;

/**
 * Maps Country domain aggregates to application responses.
 */
public final class CountryApplicationMapper {

    private CountryApplicationMapper() {
        // Utility class
    }

    /**
     * Maps a Country aggregate to a detailed response.
     */
    public static CountryResponse toResponse(Country country) {

        return new CountryResponse(
                country.getId().getValue(),
                country.getIso2Code().getValue(),
                country.getIso3Code().getValue(),
                country.getNumericCode().getValue(),
                country.getName().getValue(),
                country.getOfficialName().getValue(),
                country.isActive()
        );
    }

    /**
     * Maps a Country aggregate to a summary response.
     */
    public static CountrySummary toSummary(Country country) {

return new CountrySummary(
        country.getId().getValue(),
        country.getIso2Code().getValue(),
        country.getIso3Code().getValue(),
        country.getNumericCode().getValue(),
        country.getName().getValue(),
        country.getOfficialName().getValue(),
        country.isActive()
);
    }

}