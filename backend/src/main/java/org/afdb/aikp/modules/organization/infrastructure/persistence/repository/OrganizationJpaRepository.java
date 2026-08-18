package org.afdb.aikp.modules.organization.infrastructure.persistence.repository;

import org.afdb.aikp.modules.organization.infrastructure.persistence.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Organization entities.
 */
public interface OrganizationJpaRepository
        extends JpaRepository<OrganizationEntity, UUID> {

    Optional<OrganizationEntity> findByCode(String code);

    List<OrganizationEntity> findByCountryId(UUID countryId);

    List<OrganizationEntity> findByType(OrganizationType type);

    List<OrganizationEntity> findByActiveTrue();

    boolean existsByCode(String code);
}
