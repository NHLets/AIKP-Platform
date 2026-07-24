package org.afdb.aikp.modules.country.domain.model;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;
import org.afdb.aikp.shared.domain.AggregateRoot;

import java.util.Objects;

/**
 * Country Aggregate Root.
 *
 * <p>
 * Represents a sovereign country according to ISO 3166.
 * The aggregate guarantees that a Country is always in a valid state.
 * </p>
 */
public final class Country extends AggregateRoot<CountryId> {

    private Iso2Code iso2Code;

    private Iso3Code iso3Code;

    private NumericCode numericCode;

    private CountryName name;

    private OfficialCountryName officialName;

    private boolean active;

    /**
     * Private constructor.
     * Instances must be created through the factory method.
     */
    private Country(
            CountryId id,
            Iso2Code iso2Code,
            Iso3Code iso3Code,
            NumericCode numericCode,
            CountryName name,
            OfficialCountryName officialName,
            boolean active) {

        super(Objects.requireNonNull(id));

        this.iso2Code = Objects.requireNonNull(iso2Code);
        this.iso3Code = Objects.requireNonNull(iso3Code);
        this.numericCode = Objects.requireNonNull(numericCode);
        this.name = Objects.requireNonNull(name);
        this.officialName = Objects.requireNonNull(officialName);
        this.active = active;
    }

    /**
     * Factory method used to create a new Country.
     */
    public static Country create(
            CountryId id,
            Iso2Code iso2Code,
            Iso3Code iso3Code,
            NumericCode numericCode,
            CountryName name,
            OfficialCountryName officialName) {

        return new Country(
                id,
                iso2Code,
                iso3Code,
                numericCode,
                name,
                officialName,
                true
        );
    }
    /**
    * Rebuilds an existing Country aggregate from persistence.
    */
    public static Country restore(
        CountryId id,
        Iso2Code iso2Code,
        Iso3Code iso3Code,
        NumericCode numericCode,
        CountryName name,
        OfficialCountryName officialName,
        boolean active) {

    return new Country(
            id,
            iso2Code,
            iso3Code,
            numericCode,
            name,
            officialName,
            active
    );
}
    /**
     * Changes the country's display names.
     */
    public void rename(
            CountryName name,
            OfficialCountryName officialName) {

        this.name = Objects.requireNonNull(name);
        this.officialName = Objects.requireNonNull(officialName);
    }

    /**
     * Changes the ISO codes.
     */
    public void changeCodes(
            Iso2Code iso2Code,
            Iso3Code iso3Code,
            NumericCode numericCode) {

        this.iso2Code = Objects.requireNonNull(iso2Code);
        this.iso3Code = Objects.requireNonNull(iso3Code);
        this.numericCode = Objects.requireNonNull(numericCode);
    }

    /**
     * Activates the country.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Deactivates the country.
     */
    public void deactivate() {
        this.active = false;
    }

    public CountryId getCountryId() {
        return getId();
    }

    public Iso2Code getIso2Code() {
        return iso2Code;
    }

    public Iso3Code getIso3Code() {
        return iso3Code;
    }

    public NumericCode getNumericCode() {
        return numericCode;
    }

    public CountryName getName() {
        return name;
    }

    public OfficialCountryName getOfficialName() {
        return officialName;
    }

    public boolean isActive() {
        return active;
    }

}