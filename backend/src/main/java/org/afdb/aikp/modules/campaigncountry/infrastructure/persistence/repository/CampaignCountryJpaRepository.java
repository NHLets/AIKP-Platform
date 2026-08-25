package org.afdb.aikp.modules.campaigncountry.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.afdb.aikp.modules.campaigncountry.infrastructure.persistence.entity.CampaignCountryEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignCountryJpaRepository
        extends JpaRepository<CampaignCountryEntity, UUID> {

    List<CampaignCountryEntity> findByCampaignId(
            UUID campaignId);

    Optional<CampaignCountryEntity> findByCampaignIdAndCountryId(
            UUID campaignId,
            UUID countryId);

    boolean existsByCampaignIdAndCountryId(
            UUID campaignId,
            UUID countryId);
}