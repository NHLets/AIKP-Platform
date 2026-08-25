package org.afdb.aikp.modules.campaigncountry.infrastructure.persistence.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.campaigncountry.domain.model.CampaignCountry;
import org.afdb.aikp.modules.campaigncountry.domain.repository.CampaignCountryRepository;
import org.afdb.aikp.modules.campaigncountry.domain.valueobject.CampaignCountryId;
import org.afdb.aikp.modules.campaigncountry.infrastructure.persistence.entity.CampaignCountryEntity;
import org.afdb.aikp.modules.campaigncountry.infrastructure.persistence.mapper.CampaignCountryPersistenceMapper;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CampaignCountryRepositoryAdapter
        implements CampaignCountryRepository {

    private final CampaignCountryJpaRepository jpaRepository;

    private final CampaignCountryPersistenceMapper mapper;

    public CampaignCountryRepositoryAdapter(
            CampaignCountryJpaRepository jpaRepository,
            CampaignCountryPersistenceMapper mapper) {

        this.jpaRepository = Objects.requireNonNull(
                jpaRepository,
                "CampaignCountryJpaRepository cannot be null.");

        this.mapper = Objects.requireNonNull(
                mapper,
                "CampaignCountryPersistenceMapper cannot be null.");
    }

    @Override
    public CampaignCountry save(
            CampaignCountry campaignCountry) {

        Objects.requireNonNull(
                campaignCountry,
                "CampaignCountry cannot be null.");

        CampaignCountryEntity entity =
                mapper.toEntity(campaignCountry);

        CampaignCountryEntity savedEntity =
                jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CampaignCountry> findById(
            CampaignCountryId id) {

        Objects.requireNonNull(
                id,
                "CampaignCountry ID cannot be null.");

        return jpaRepository.findById(
                        id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampaignCountry> findByCampaignId(
            CampaignId campaignId) {

        Objects.requireNonNull(
                campaignId,
                "Campaign ID cannot be null.");

        return jpaRepository.findByCampaignId(
                        campaignId.getValue())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CampaignCountry> findByCampaignIdAndCountryId(
            CampaignId campaignId,
            CountryId countryId) {

        Objects.requireNonNull(
                campaignId,
                "Campaign ID cannot be null.");

        Objects.requireNonNull(
                countryId,
                "Country ID cannot be null.");

        return jpaRepository
                .findByCampaignIdAndCountryId(
                        campaignId.getValue(),
                        countryId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCampaignIdAndCountryId(
            CampaignId campaignId,
            CountryId countryId) {

        Objects.requireNonNull(
                campaignId,
                "Campaign ID cannot be null.");

        Objects.requireNonNull(
                countryId,
                "Country ID cannot be null.");

        return jpaRepository
                .existsByCampaignIdAndCountryId(
                        campaignId.getValue(),
                        countryId.getValue());
    }

    @Override
    public void delete(
            CampaignCountry campaignCountry) {

        Objects.requireNonNull(
                campaignCountry,
                "CampaignCountry cannot be null.");

        jpaRepository.deleteById(
                campaignCountry.getId().getValue());

        jpaRepository.flush();
    }
}
