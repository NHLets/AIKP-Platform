package org.afdb.aikp.modules.organization.domain.model;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;
import org.afdb.aikp.modules.organization.domain.enums.OrganizationType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrganizationTest {

    private static final CountryId COUNTRY_ID =
            CountryId.generate();

    private Organization createOrganization() {

        return Organization.create(
                OrganizationId.generate(),
                OrganizationCode.of("AFDB"),
                OrganizationName.of("African Development Bank"),
                OrganizationType.GOVERNMENT_AGENCY,
                COUNTRY_ID
        );
    }

    @Test
    void shouldCreateOrganizationAsActive() {

        Organization organization = createOrganization();

        assertThat(organization.getOrganizationId()).isNotNull();
        assertThat(organization.getCode().getValue()).isEqualTo("AFDB");
        assertThat(organization.getName().getValue())
                .isEqualTo("African Development Bank");
        assertThat(organization.getType())
                .isEqualTo(OrganizationType.GOVERNMENT_AGENCY);
        assertThat(organization.getCountryId())
                .isEqualTo(COUNTRY_ID);
        assertThat(organization.isActive()).isTrue();
    }

    @Test
    void shouldRestoreOrganizationWithPersistedState() {

        OrganizationId id = OrganizationId.generate();

        Organization organization = Organization.restore(
                id,
                OrganizationCode.of("COMESA"),
                OrganizationName.of(
                        "Common Market for Eastern and Southern Africa"),
                OrganizationType.MINISTRY,
                COUNTRY_ID,
                false
        );

        assertThat(organization.getOrganizationId()).isEqualTo(id);
        assertThat(organization.isActive()).isFalse();
    }

    @Test
    void shouldChangeCode() {

        Organization organization = createOrganization();

        organization.changeCode(
                OrganizationCode.of("AFDB-ENERGY"));

        assertThat(organization.getCode().getValue())
                .isEqualTo("AFDB-ENERGY");
    }

    @Test
    void shouldRenameOrganization() {

        Organization organization = createOrganization();

        organization.rename(
                OrganizationName.of("African Development Bank Group"));

        assertThat(organization.getName().getValue())
                .isEqualTo("African Development Bank Group");
    }

    @Test
    void shouldChangeOrganizationType() {

        Organization organization = createOrganization();

        organization.changeType(
                OrganizationType.MINISTRY);

        assertThat(organization.getType())
                .isEqualTo(OrganizationType.MINISTRY);
    }

    @Test
    void shouldChangeCountry() {

        Organization organization = createOrganization();

        CountryId newCountryId = CountryId.generate();

        organization.changeCountry(newCountryId);

        assertThat(organization.getCountryId())
                .isEqualTo(newCountryId);
    }

    @Test
    void shouldDeactivateOrganization() {

        Organization organization = createOrganization();

        organization.deactivate();

        assertThat(organization.isActive()).isFalse();
    }

    @Test
    void shouldActivateOrganization() {

        Organization organization = createOrganization();

        organization.deactivate();
        organization.activate();

        assertThat(organization.isActive()).isTrue();
    }

    @Test
    void shouldRejectNullCode() {

        assertThatThrownBy(() ->
                Organization.create(
                        OrganizationId.generate(),
                        null,
                        OrganizationName.of("Test Organization"),
                        OrganizationType.REGULATOR,
                        COUNTRY_ID
                ))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullName() {

        assertThatThrownBy(() ->
                Organization.create(
                        OrganizationId.generate(),
                        OrganizationCode.of("TEST"),
                        null,
                        OrganizationType.REGULATOR,
                        COUNTRY_ID
                ))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullType() {

        assertThatThrownBy(() ->
                Organization.create(
                        OrganizationId.generate(),
                        OrganizationCode.of("TEST"),
                        OrganizationName.of("Test Organization"),
                        null,
                        COUNTRY_ID
                ))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullCountry() {

        assertThatThrownBy(() ->
                Organization.create(
                        OrganizationId.generate(),
                        OrganizationCode.of("TEST"),
                        OrganizationName.of("Test Organization"),
                        OrganizationType.REGULATOR,
                        null
                ))
                .isInstanceOf(NullPointerException.class);
    }
}
