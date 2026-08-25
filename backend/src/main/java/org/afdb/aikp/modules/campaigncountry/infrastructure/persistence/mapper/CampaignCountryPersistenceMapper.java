package org.afdb.aikp.modules.campaigncountry.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaigncountry.domain.model.CampaignCountry;
import org.afdb.aikp.modules.campaigncountry.domain.valueobject.CampaignCountryId;
import org.afdb.aikp.modules.campaigncountry.infrastructure.persistence.entity.CampaignCountryEntity;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

import org.springframework.stereotype.Component;

/**
 * Maps CampaignCountry domain objects
 * to and from persistence entities.
 */
@Component
public class CampaignCountryPersistenceMapper {

    /**
     * Converts a CampaignCountry domain object
     * into a CampaignCountryEntity.
     *
     * @param campaignCountry domain object
     * @return persistence entity
     */
    public CampaignCountryEntity toEntity(
            CampaignCountry campaignCountry) {

        return new CampaignCountryEntity(
                campaignCountry.getId().getValue(),
                campaignCountry.getCampaignId().getValue(),
                campaignCountry.getCountryId().getValue(),
                campaignCountry.getCreatedAt(),
                campaignCountry.getUpdatedAt()
        );
    }

    /**
     * Converts a CampaignCountryEntity
     * into a CampaignCountry domain object.
     *
     * @param entity persistence entity
     * @return domain object
     */
    public CampaignCountry toDomain(
            CampaignCountryEntity entity) {

        return CampaignCountry.reconstitute(
                CampaignCountryId.of(entity.getId()),
                CampaignId.of(entity.getCampaignId()),
                CountryId.of(entity.getCountryId()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}