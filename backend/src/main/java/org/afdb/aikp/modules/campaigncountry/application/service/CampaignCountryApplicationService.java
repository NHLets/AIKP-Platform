package org.afdb.aikp.modules.campaigncountry.application.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.afdb.aikp.modules.campaign.domain.exception.CampaignNotFoundException;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;

import org.afdb.aikp.modules.campaigncountry.application.command.AddCountryToCampaignCommand;
import org.afdb.aikp.modules.campaigncountry.application.command.RemoveCountryFromCampaignCommand;
import org.afdb.aikp.modules.campaigncountry.application.mapper.CampaignCountryApplicationMapper;
import org.afdb.aikp.modules.campaigncountry.application.query.GetCampaignCountriesQuery;
import org.afdb.aikp.modules.campaigncountry.application.query.GetCampaignCountryQuery;
import org.afdb.aikp.modules.campaigncountry.application.response.CampaignCountryResponse;

import org.afdb.aikp.modules.campaigncountry.domain.exception.CampaignCountryNotFoundException;
import org.afdb.aikp.modules.campaigncountry.domain.exception.CountryAlreadyAssignedToCampaignException;
import org.afdb.aikp.modules.campaigncountry.domain.model.CampaignCountry;
import org.afdb.aikp.modules.campaigncountry.domain.repository.CampaignCountryRepository;

import org.afdb.aikp.modules.country.domain.exception.CountryNotFoundException;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for managing country participation
 * in campaigns.
 */
@Service
@Transactional
public class CampaignCountryApplicationService {

    private final CampaignRepository campaignRepository;

    private final CountryRepository countryRepository;

    private final CampaignCountryRepository campaignCountryRepository;

    private final CampaignCountryApplicationMapper mapper;

    public CampaignCountryApplicationService(
            CampaignRepository campaignRepository,
            CountryRepository countryRepository,
            CampaignCountryRepository campaignCountryRepository,
            CampaignCountryApplicationMapper mapper) {

        this.campaignRepository = Objects.requireNonNull(
                campaignRepository,
                "CampaignRepository cannot be null.");

        this.countryRepository = Objects.requireNonNull(
                countryRepository,
                "CountryRepository cannot be null.");

        this.campaignCountryRepository = Objects.requireNonNull(
                campaignCountryRepository,
                "CampaignCountryRepository cannot be null.");

        this.mapper = Objects.requireNonNull(
                mapper,
                "CampaignCountryApplicationMapper cannot be null.");
    }

    /**
     * Adds a country to a campaign.
     */
    public CampaignCountryResponse addCountryToCampaign(
            AddCountryToCampaignCommand command) {

        Objects.requireNonNull(
                command,
                "AddCountryToCampaignCommand cannot be null.");

        CampaignId campaignId =
                CampaignId.of(command.campaignId());

        CountryId countryId =
                CountryId.of(command.countryId());

        if (!campaignRepository.existsById(campaignId)) {
            throw new CampaignNotFoundException(
                    "Campaign not found with id: "
                            + campaignId.getValue());
        }

        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(
                    countryId);
        }

        if (campaignCountryRepository
                .existsByCampaignIdAndCountryId(
                        campaignId,
                        countryId)) {

            throw new CountryAlreadyAssignedToCampaignException(
                    campaignId.getValue(),
                    countryId.getValue());
        }

        CampaignCountry campaignCountry =
                CampaignCountry.create(
                        campaignId,
                        countryId);

        CampaignCountry savedCampaignCountry =
                campaignCountryRepository.save(
                        campaignCountry);

        return mapper.toResponse(
                savedCampaignCountry);
    }

    /**
     * Retrieves all countries associated with a campaign.
     */
    @Transactional(readOnly = true)
    public List<CampaignCountryResponse> getCampaignCountries(
            GetCampaignCountriesQuery query) {

        Objects.requireNonNull(
                query,
                "GetCampaignCountriesQuery cannot be null.");

        CampaignId campaignId =
                CampaignId.of(query.campaignId());

        if (!campaignRepository.existsById(campaignId)) {
            throw new CampaignNotFoundException(
                    "Campaign not found with id: "
                            + campaignId.getValue());
        }

        return campaignCountryRepository
                .findByCampaignId(campaignId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific country association
     * within a campaign.
     */
    @Transactional(readOnly = true)
    public CampaignCountryResponse getCampaignCountry(
            GetCampaignCountryQuery query) {

        Objects.requireNonNull(
                query,
                "GetCampaignCountryQuery cannot be null.");

        CampaignId campaignId =
                CampaignId.of(query.campaignId());

        CountryId countryId =
                CountryId.of(query.countryId());

        return campaignCountryRepository
                .findByCampaignIdAndCountryId(
                        campaignId,
                        countryId)
                .map(mapper::toResponse)
                .orElseThrow(() ->
                        new CampaignCountryNotFoundException(
                                campaignId.getValue(),
                                countryId.getValue()));
    }

    /**
     * Removes a country from a campaign.
     */
    public void removeCountryFromCampaign(
            RemoveCountryFromCampaignCommand command) {

        Objects.requireNonNull(
                command,
                "RemoveCountryFromCampaignCommand cannot be null.");

        CampaignId campaignId =
                CampaignId.of(command.campaignId());

        CountryId countryId =
                CountryId.of(command.countryId());

        CampaignCountry campaignCountry =
                campaignCountryRepository
                        .findByCampaignIdAndCountryId(
                                campaignId,
                                countryId)
                        .orElseThrow(() ->
                                new CampaignCountryNotFoundException(
                                        campaignId.getValue(),
                                        countryId.getValue()));

        campaignCountryRepository.delete(
                campaignCountry);
    }
}
