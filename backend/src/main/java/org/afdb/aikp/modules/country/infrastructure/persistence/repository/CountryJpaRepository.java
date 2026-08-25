package org.afdb.aikp.modules.country.infrastructure.persistence.repository;

import org.afdb.aikp.modules.country.infrastructure.persistence.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Country entities.
 */
public interface CountryJpaRepository
        extends JpaRepository<CountryEntity, UUID> {

    Optional<CountryEntity> findByIso2Code(String iso2Code);

    Optional<CountryEntity> findByIso3Code(String iso3Code);

    Optional<CountryEntity> findByNumericCode(String numericCode);

    List<CountryEntity> findByActiveTrue();

    long countByActiveTrue();

    boolean existsByIso2Code(String iso2Code);

    boolean existsByIso3Code(String iso3Code);

    boolean existsByNumericCode(String numericCode);

}