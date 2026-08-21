package org.afdb.aikp.modules.organization.infrastructure.persistence.adapter;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.infrastructure.persistence.entity.OrganizationEntity;
import org.afdb.aikp.modules.organization.infrastructure.persistence.mapper.OrganizationPersistenceMapper;
import org.afdb.aikp.modules.organization.infrastructure.persistence.repository.OrganizationJpaRepository;
import org.springframework.stereotype.Repository;
import org.afdb.aikp.modules.organization.domain.enums.OrganizationType;

import java.util.List;
import java.util.Optional;

@Repository
public class OrganizationRepositoryAdapter
        implements OrganizationRepository {

    private final OrganizationJpaRepository repository;

    public OrganizationRepositoryAdapter(
            OrganizationJpaRepository repository) {

        this.repository = repository;
    }

    @Override
    public Organization save(Organization organization) {

        OrganizationId id =
                organization.getOrganizationId();

        OrganizationEntity entity =
                repository.findById(id.getValue())

                        .map(existing -> {

                            OrganizationPersistenceMapper
                                    .updateEntity(
                                            existing,
                                            organization);

                            return existing;
                        })

                        .orElseGet(() ->
                                OrganizationPersistenceMapper
                                        .toEntity(organization));

        OrganizationEntity saved =
                repository.save(entity);

        return OrganizationPersistenceMapper
                .toDomain(saved);
    }

    @Override
    public Optional<Organization> findById(
            OrganizationId id) {

        return repository.findById(id.getValue())
                .map(OrganizationPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Organization> findByCode(
            OrganizationCode code) {

        return repository.findByCode(code.getValue())
                .map(OrganizationPersistenceMapper::toDomain);
    }

    @Override
    public List<Organization> findByCountryId(
            CountryId countryId) {

        return repository.findByCountryId(
                        countryId.getValue())
                .stream()
                .map(OrganizationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Organization> findByType(
        OrganizationType type) {

    return repository.findByType(type)
            .stream()
            .map(OrganizationPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public List<Organization> findAll() {

        return repository.findAll()
                .stream()
                .map(OrganizationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Organization> findActive() {

        return repository.findByActiveTrue()
                .stream()
                .map(OrganizationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(
            OrganizationId id) {

        return repository.existsById(id.getValue());
    }

    @Override
    public boolean existsByCode(
            OrganizationCode code) {

        return repository.existsByCode(
                code.getValue());
    }

    @Override
    public void delete(Organization organization) {

        repository.findById(
                        organization
                                .getOrganizationId()
                                .getValue())
                .ifPresent(repository::delete);
    }
}
