package org.afdb.aikp.modules.country.infrastructure.persistence.adapter;

import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.infrastructure.persistence.entity.CountryEntity;
import org.afdb.aikp.modules.country.infrastructure.persistence.mapper.CountryPersistenceMapper;
import org.afdb.aikp.modules.country.infrastructure.persistence.repository.CountryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CountryRepositoryAdapter implements CountryRepository {

    private final CountryJpaRepository repository;

    public CountryRepositoryAdapter(CountryJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Country save(Country country) {

        CountryId id = country.getCountryId();

        CountryEntity entity = repository.findById(id.getValue())

                .map(existing -> {

                    CountryPersistenceMapper.updateEntity(existing, country);

                    return existing;

                })

                .orElseGet(() ->
                        CountryPersistenceMapper.toEntity(country));

        CountryEntity saved = repository.save(entity);

        return CountryPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Country> findById(CountryId id) {

        return repository.findById(id.getValue())
                .map(CountryPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Country> findByIso2Code(Iso2Code iso2Code) {

        return repository.findByIso2Code(iso2Code.getValue())
                .map(CountryPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Country> findByIso3Code(Iso3Code iso3Code) {

        return repository.findByIso3Code(iso3Code.getValue())
                .map(CountryPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Country> findByNumericCode(NumericCode numericCode) {

        return repository.findByNumericCode(numericCode.getValue())
                .map(CountryPersistenceMapper::toDomain);
    }

    @Override
    public List<Country> findAll() {

        return repository.findAll()
                .stream()
                .map(CountryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Country> findActive() {

        return repository.findByActiveTrue()
                .stream()
                .map(CountryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(CountryId id) {

        return repository.existsById(id.getValue());
    }

    @Override
    public boolean existsByIso2Code(Iso2Code iso2Code) {

        return repository.existsByIso2Code(iso2Code.getValue());
    }

    @Override
    public boolean existsByIso3Code(Iso3Code iso3Code) {

        return repository.existsByIso3Code(iso3Code.getValue());
    }

    @Override
    public boolean existsByNumericCode(NumericCode numericCode) {

        return repository.existsByNumericCode(numericCode.getValue());
    }

    @Override
    public void delete(Country country) {

        repository.findById(country.getCountryId().getValue())
                .ifPresent(repository::delete);
    }

}