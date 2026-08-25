package org.afdb.aikp.modules.collection.domain.repository;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

/**
 * Repository abstraction for DataCollection aggregates.
 */
public interface DataCollectionRepository {

    DataCollection save(DataCollection dataCollection);

    Optional<DataCollection> findById(
            DataCollectionId id);

    List<DataCollection> findAll();

    List<DataCollection> findByCampaignId(
            CampaignId campaignId);

    List<DataCollection> findByCountryId(
            CountryId countryId);

    boolean existsById(
            DataCollectionId id);

    void delete(
            DataCollection dataCollection);
}
