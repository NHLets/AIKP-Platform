package org.afdb.aikp.modules.collection.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionRepository;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.infrastructure.persistence.entity.DataCollectionEntity;
import org.afdb.aikp.modules.collection.infrastructure.persistence.mapper.DataCollectionPersistenceMapper;
import org.afdb.aikp.modules.collection.infrastructure.persistence.repository.DataCollectionJpaRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

import org.springframework.stereotype.Repository;

/**
 * Persistence adapter implementing the DataCollectionRepository
 * domain abstraction.
 */
@Repository
public class DataCollectionRepositoryAdapter
        implements DataCollectionRepository {

    private final DataCollectionJpaRepository
            dataCollectionJpaRepository;

    private final DataCollectionPersistenceMapper mapper;

    public DataCollectionRepositoryAdapter(
            DataCollectionJpaRepository
                    dataCollectionJpaRepository,
            DataCollectionPersistenceMapper mapper) {

        this.dataCollectionJpaRepository =
                dataCollectionJpaRepository;

        this.mapper = mapper;
    }

    @Override
    public DataCollection save(
            DataCollection dataCollection) {

        DataCollectionEntity entity =
                mapper.toEntity(dataCollection);

        DataCollectionEntity savedEntity =
                dataCollectionJpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<DataCollection> findById(
            DataCollectionId id) {

        return dataCollectionJpaRepository
                .findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<DataCollection> findAll() {

        return dataCollectionJpaRepository
                .findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<DataCollection> findByCampaignId(
            CampaignId campaignId) {

        return dataCollectionJpaRepository
                .findByCampaignId(
                        campaignId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<DataCollection> findByCountryId(
            CountryId countryId) {

        return dataCollectionJpaRepository
                .findByCountryId(
                        countryId.getValue())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(
            DataCollectionId id) {

        return dataCollectionJpaRepository
                .existsById(id.getValue());
    }

    @Override
    public void delete(
            DataCollection dataCollection) {

        dataCollectionJpaRepository.deleteById(
                dataCollection.getDataCollectionId()
                        .getValue());
    }
}
