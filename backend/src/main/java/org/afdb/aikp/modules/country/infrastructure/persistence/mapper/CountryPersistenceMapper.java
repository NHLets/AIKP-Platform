package org.afdb.aikp.modules.country.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;
import org.afdb.aikp.modules.country.infrastructure.persistence.entity.CountryEntity;

/**
 * Maps Country aggregates to and from JPA entities.
 */
public final class CountryPersistenceMapper {

    private CountryPersistenceMapper() {
    }

    /**
     * Creates a new JPA entity.
     * Used only during CREATE.
     */
    public static CountryEntity toEntity(Country country) {

        if (country == null) {
            return null;
        }

        return new CountryEntity(
                country.getCountryId().getValue(),
                country.getIso2Code().getValue(),
                country.getIso3Code().getValue(),
                country.getNumericCode().getValue(),
                country.getName().getValue(),
                country.getOfficialName().getValue(),
                country.isActive()
        );
    }

    /**
     * Updates an existing managed JPA entity.
     * Used only during UPDATE.
     */
    public static void updateEntity(
            CountryEntity entity,
            Country country) {

        if (entity == null || country == null) {
            return;
        }

        entity.setIso2Code(country.getIso2Code().getValue());
        entity.setIso3Code(country.getIso3Code().getValue());
        entity.setNumericCode(country.getNumericCode().getValue());
        entity.setName(country.getName().getValue());
        entity.setOfficialName(country.getOfficialName().getValue());
        entity.setActive(country.isActive());

        /*
         * Never update:
         *  - id
         *  - version
         *  - createdAt
         *  - createdBy
         *  - updatedAt
         *  - updatedBy
         *
         * Hibernate manages them automatically.
         */
    }

    /**
     * Converts a JPA entity into a domain aggregate.
     */
    public static Country toDomain(CountryEntity entity) {

        if (entity == null) {
            return null;
        }

        return Country.restore(
                CountryId.of(entity.getId()),
                Iso2Code.of(entity.getIso2Code()),
                Iso3Code.of(entity.getIso3Code()),
                NumericCode.of(entity.getNumericCode()),
                CountryName.of(entity.getName()),
                OfficialCountryName.of(entity.getOfficialName()),
                entity.isActive()
        );
    }
}