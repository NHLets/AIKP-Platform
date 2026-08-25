package org.afdb.aikp.modules.campaign.infrastructure.persistence.repository;

import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaign.infrastructure.persistence.entity.CampaignEntity;
import org.afdb.aikp.modules.campaign.infrastructure.persistence.mapper.CampaignPersistenceMapper;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of the Campaign domain repository.
 */
@Repository
@Transactional
public class CampaignRepositoryImpl implements CampaignRepository {

    private final CampaignJpaRepository jpaRepository;

    public CampaignRepositoryImpl(
            CampaignJpaRepository jpaRepository) {

        this.jpaRepository = jpaRepository;
    }

    @Override
    public Campaign save(Campaign campaign) {

        if (campaign == null) {
            throw new IllegalArgumentException(
                    "Campaign cannot be null.");
        }

        CampaignEntity entity = jpaRepository
                .findById(campaign.getId().getValue())
                .orElseGet(() ->
                        new CampaignEntity(
                                campaign.getId().getValue()));

        entity.setCode(
                campaign.getCode().getValue());

        entity.setName(
                campaign.getName().getValue());

        entity.setDescription(
                campaign.getDescription().getValue());

        entity.setStartDate(
                campaign.getStartDate());

        entity.setEndDate(
                campaign.getEndDate());

        entity.setStatus(
                campaign.getStatus());

        CampaignEntity saved =
                jpaRepository.save(entity);

        return CampaignPersistenceMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Campaign> findById(
            CampaignId id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Campaign id cannot be null.");
        }

        return jpaRepository
                .findById(id.getValue())
                .map(CampaignPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(
        CampaignId id) {

    if (id == null) {
        throw new IllegalArgumentException(
                "Campaign id cannot be null.");
    }

    return jpaRepository.existsById(
            id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Campaign> findByCode(
            CampaignCode code) {

        if (code == null) {
            throw new IllegalArgumentException(
                    "Campaign code cannot be null.");
        }

        return jpaRepository
                .findByCode(code.getValue())
                .map(CampaignPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(
            CampaignCode code) {

        if (code == null) {
            throw new IllegalArgumentException(
                    "Campaign code cannot be null.");
        }

        return jpaRepository.existsByCode(
                code.getValue());
    }

    @Override
    public void delete(Campaign campaign) {

        if (campaign == null) {
            throw new IllegalArgumentException(
                    "Campaign cannot be null.");
        }

        jpaRepository.deleteById(
                campaign.getId().getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Campaign> findAll() {

        return jpaRepository
                .findAll()
                .stream()
                .map(CampaignPersistenceMapper::toDomain)
                .toList();
    }
}