package org.afdb.aikp.modules.campaign.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;
import org.afdb.aikp.modules.campaign.infrastructure.persistence.entity.CampaignEntity;

/**
 * Maps the Campaign domain aggregate to and from its persistence model.
 *
 * <p>The mapper deliberately keeps persistence concerns outside the domain
 * model.</p>
 */
public final class CampaignPersistenceMapper {

    private CampaignPersistenceMapper() {
        // Utility class.
    }

    /**
     * Converts a domain Campaign into a JPA entity.
     *
     * <p>Creation and update timestamps are managed by the persistence
     * layer. Existing audit values are preserved when updating an entity.</p>
     */
    public static CampaignEntity toEntity(
            Campaign campaign) {

        if (campaign == null) {
            throw new IllegalArgumentException(
                    "Campaign cannot be null.");
        }

        CampaignEntity entity =
        new CampaignEntity(campaign.getId().getValue());;
        entity.setCode(campaign.getCode().getValue());
        entity.setName(campaign.getName().getValue());
        entity.setDescription(campaign.getDescription().getValue());

        entity.setStartDate(campaign.getStartDate());
        entity.setEndDate(campaign.getEndDate());
        entity.setStatus(campaign.getStatus());

        return entity;
    }

    /**
     * Reconstructs a Campaign domain aggregate from its persistence entity.
     *
     * <p>The aggregate restore factory is deliberately used so that
     * reconstruction remains governed by the domain model.</p>
     */
    public static Campaign toDomain(
            CampaignEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Campaign entity cannot be null.");
        }

        return Campaign.restore(
                CampaignId.of(entity.getId()),
                CampaignCode.of(entity.getCode()),
                CampaignName.of(entity.getName()),
                CampaignDescription.of(entity.getDescription()),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus());
    }
}
