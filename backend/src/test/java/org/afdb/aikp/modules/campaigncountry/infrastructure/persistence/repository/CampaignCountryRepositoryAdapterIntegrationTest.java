package org.afdb.aikp.modules.campaigncountry.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;

import org.afdb.aikp.modules.campaigncountry.domain.model.CampaignCountry;
import org.afdb.aikp.modules.campaigncountry.domain.repository.CampaignCountryRepository;

import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CampaignCountryRepositoryAdapterIntegrationTest {

    private static final AtomicInteger
            COUNTRY_SEQUENCE =
                    new AtomicInteger(10000);

    @Autowired
    private CampaignCountryRepository
            campaignCountryRepository;

    @Autowired
    private CampaignCountryJpaRepository
            campaignCountryJpaRepository;

    @Autowired
    private CampaignRepository
            campaignRepository;

    @Autowired
    private CountryRepository
            countryRepository;

    @AfterEach
    void cleanUp() {

        campaignCountryJpaRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindCampaignCountryById() {

        Campaign campaign =
                createAndSaveCampaign();

        Country country =
                createAndSaveCountry();

        CampaignCountry saved =
                campaignCountryRepository.save(
                        CampaignCountry.create(
                                campaign.getId(),
                                country.getCountryId()));

        assertThat(
                campaignCountryRepository.findById(
                        saved.getId()))
                .isPresent()
                .hasValueSatisfying(found -> {

                    assertThat(found.getId())
                            .isEqualTo(saved.getId());

                    assertThat(found.getCampaignId())
                            .isEqualTo(
                                    campaign.getId());

                    assertThat(found.getCountryId())
                            .isEqualTo(
                                    country.getCountryId());
                });
    }

    @Test
    void shouldFindCampaignCountriesByCampaignId() {

        Campaign campaign =
                createAndSaveCampaign();

        Country firstCountry =
                createAndSaveCountry();

        Country secondCountry =
                createAndSaveCountry();

        CampaignCountry first =
                campaignCountryRepository.save(
                        CampaignCountry.create(
                                campaign.getId(),
                                firstCountry.getCountryId()));

        CampaignCountry second =
                campaignCountryRepository.save(
                        CampaignCountry.create(
                                campaign.getId(),
                                secondCountry.getCountryId()));

        List<CampaignCountry> campaignCountries =
                campaignCountryRepository.findByCampaignId(
                        campaign.getId());

        assertThat(campaignCountries)
                .extracting(CampaignCountry::getId)
                .containsExactlyInAnyOrder(
                        first.getId(),
                        second.getId());
    }

    @Test
    void shouldFindCampaignCountryByCampaignIdAndCountryId() {

        Campaign campaign =
                createAndSaveCampaign();

        Country country =
                createAndSaveCountry();

        CampaignCountry saved =
                campaignCountryRepository.save(
                        CampaignCountry.create(
                                campaign.getId(),
                                country.getCountryId()));

        assertThat(
                campaignCountryRepository
                        .findByCampaignIdAndCountryId(
                                campaign.getId(),
                                country.getCountryId()))
                .isPresent()
                .hasValueSatisfying(found ->
                        assertThat(found.getId())
                                .isEqualTo(
                                        saved.getId()));
    }

    @Test
    void shouldReturnTrueWhenCampaignCountryExists() {

        Campaign campaign =
                createAndSaveCampaign();

        Country country =
                createAndSaveCountry();

        campaignCountryRepository.save(
                CampaignCountry.create(
                        campaign.getId(),
                        country.getCountryId()));

        boolean exists =
                campaignCountryRepository
                        .existsByCampaignIdAndCountryId(
                                campaign.getId(),
                                country.getCountryId());

        assertThat(exists)
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenCampaignCountryDoesNotExist() {

        boolean exists =
                campaignCountryRepository
                        .existsByCampaignIdAndCountryId(
                                CampaignId.of(
                                        UUID.randomUUID()),
                                CountryId.of(
                                        UUID.randomUUID()));

        assertThat(exists)
                .isFalse();
    }

    @Test
    void shouldDeleteCampaignCountry() {

        Campaign campaign =
                createAndSaveCampaign();

        Country country =
                createAndSaveCountry();

        CampaignCountry saved =
                campaignCountryRepository.save(
                        CampaignCountry.create(
                                campaign.getId(),
                                country.getCountryId()));

        campaignCountryRepository.delete(saved);

        assertThat(
                campaignCountryRepository.findById(
                        saved.getId()))
                .isEmpty();
    }

    private Campaign createAndSaveCampaign() {

        String suffix =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();

        Campaign campaign =
                Campaign.create(
                        CampaignCode.of(
                                "CAMPAIGN_"
                                        + suffix),
                        CampaignName.of(
                                "Test Campaign "
                                        + suffix),
                        CampaignDescription.of(
                                "Campaign created for "
                                        + "CampaignCountry "
                                        + "integration tests."),
                        LocalDate.of(
                                2026,
                                1,
                                1),
                        LocalDate.of(
                                2026,
                                12,
                                31));

        return campaignRepository.save(
                campaign);
    }

    private Country createAndSaveCountry() {

        int sequence;

        String iso2Value;
        String iso3Value;
        String numericCodeValue;

        do {
            sequence =
                    COUNTRY_SEQUENCE
                            .incrementAndGet();

            iso2Value =
                    toAlphabeticCode(
                            sequence,
                            2);

            iso3Value =
                    toAlphabeticCode(
                            sequence,
                            3);

            numericCodeValue =
                    String.format(
                            "%03d",
                            sequence % 1000);

        } while (
                countryRepository
                        .existsByIso2Code(
                                Iso2Code.of(iso2Value))
                || countryRepository
                        .existsByIso3Code(
                                Iso3Code.of(iso3Value))
                || countryRepository
                        .existsByNumericCode(
                                NumericCode.of(
                                        numericCodeValue)));

        Country country =
                Country.create(
                        CountryId.generate(),
                        Iso2Code.of(iso2Value),
                        Iso3Code.of(iso3Value),
                        NumericCode.of(
                                numericCodeValue),
                        CountryName.of(
                                "Test Country "
                                        + sequence),
                        OfficialCountryName.of(
                                "Official Test Country "
                                        + sequence));

        return countryRepository.save(
                country);
    }

    private String toAlphabeticCode(
            int value,
            int length) {

        StringBuilder builder =
                new StringBuilder();

        int currentValue =
                value;

        for (int index = 0;
                index < length;
                index++) {

            char letter =
                    (char) (
                            'A'
                                    + (currentValue % 26));

            builder.append(letter);

            currentValue =
                    currentValue / 26;
        }

        return builder
                .reverse()
                .toString();
    }
}
