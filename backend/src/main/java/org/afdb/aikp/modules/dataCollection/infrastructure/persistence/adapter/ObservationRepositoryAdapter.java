package org.afdb.aikp.modules.dataCollection.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import org.afdb.aikp.modules.dataCollection.domain.model.*;
import org.afdb.aikp.modules.dataCollection.domain.repository.ObservationRepository;
import org.afdb.aikp.modules.dataCollection.infrastructure.persistence.entity.ObservationEntity;
import org.afdb.aikp.modules.dataCollection.infrastructure.persistence.mapper.ObservationPersistenceMapper;
import org.afdb.aikp.modules.dataCollection.infrastructure.persistence.repository.SpringDataObservationRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ObservationRepositoryAdapter implements ObservationRepository {

    private final SpringDataObservationRepository repository;
    private final ObservationPersistenceMapper mapper;

    @Override
    public Observation save(Observation observation) {
        ObservationEntity entity = mapper.toEntity(observation);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Observation> findById(ObservationId id) {
        return repository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Observation> findByDataCollection(UUID id) {
        return repository.findByDataCollectionId(id)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Observation> findByOrganization(UUID id) {
        return repository.findByOrganizationId(id)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(ObservationId id) {
        repository.deleteById(id.value());
    }
}
