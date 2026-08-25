package org.afdb.aikp.modules.campaigncountry.domain.repository;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaigncountry.domain.model.CampaignCountry;
import org.afdb.aikp.modules.campaigncountry.domain.valueobject.CampaignCountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

/**
 * Repository contract for CampaignCountry associations.
 */
public interface CampaignCountryRepository {

    CampaignCountry save(CampaignCountry campaignCountry);

    Optional<CampaignCountry> findById(
            CampaignCountryId id);

    List<CampaignCountry> findByCampaignId(
            CampaignId campaignId);

    Optional<CampaignCountry> findByCampaignIdAndCountryId(
            CampaignId campaignId,
            CountryId countryId);

    boolean existsByCampaignIdAndCountryId(
            CampaignId campaignId,
            CountryId countryId);

    void delete(CampaignCountry campaignCountry);
}
