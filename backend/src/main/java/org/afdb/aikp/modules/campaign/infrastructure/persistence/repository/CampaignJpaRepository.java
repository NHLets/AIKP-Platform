package org.afdb.aikp.modules.campaign.infrastructure.persistence.repository;

import org.afdb.aikp.modules.campaign.infrastructure.persistence.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import org.afdb.aikp.modules.campaign.domain.enums.CampaignStatus;

/**
 * Spring Data repository for Campaign persistence.
 */
public interface CampaignJpaRepository
        extends JpaRepository<CampaignEntity, UUID> {

    Optional<CampaignEntity> findByCode(String code);

    boolean existsByCode(String code);

    long countByStatus(CampaignStatus status);
}
