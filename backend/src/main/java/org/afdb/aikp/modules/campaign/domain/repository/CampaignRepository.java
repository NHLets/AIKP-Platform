package org.afdb.aikp.modules.campaign.domain.repository;

import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;

import java.util.Optional;
import java.util.List;

/**
 * Repository contract for the Campaign aggregate.
 *
 * <p>This interface belongs to the domain layer and deliberately contains
 * no persistence technology dependency.</p>
 */
public interface CampaignRepository {


    
    /**
     * Persists a Campaign aggregate.
     *
     * @param campaign campaign to persist
     * @return persisted campaign
     */
    Campaign save(Campaign campaign);

    /**
     * Finds a Campaign by its identifier.
     *
     * @param id campaign identifier
     * @return campaign when found
     */
    Optional<Campaign> findById(CampaignId id);

    /**
     * Finds a Campaign by its business code.
     *
     * @param code campaign business code
     * @return campaign when found
     */
    
    /**
    * Checks whether a Campaign with the given identifier exists.
    *
    * @param id campaign identifier
    * @return true when the campaign exists
    */
    boolean existsById(CampaignId id);
    
    Optional<Campaign> findByCode(CampaignCode code);

    /**
     * Checks whether a Campaign with the given business code exists.
     *
     * @param code campaign business code
     * @return true when the code already exists
     */
    boolean existsByCode(CampaignCode code);

    /**
    * Retrieves all Campaign aggregates.
    *
    * @return list of campaigns
    */
    List<Campaign> findAll();
    
    /**
     * Deletes a Campaign aggregate.
     *
     * @param campaign campaign to delete
     */
    void delete(Campaign campaign);
}
