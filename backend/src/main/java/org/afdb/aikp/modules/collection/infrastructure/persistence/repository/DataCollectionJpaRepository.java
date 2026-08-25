package org.afdb.aikp.modules.collection.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for DataCollectionEntity.
 */
@Repository
public interface DataCollectionJpaRepository
        extends JpaRepository<DataCollectionEntity, UUID> {

    List<DataCollectionEntity> findByCampaignId(
            UUID campaignId);

    List<DataCollectionEntity> findByCountryId(
            UUID countryId);
}
