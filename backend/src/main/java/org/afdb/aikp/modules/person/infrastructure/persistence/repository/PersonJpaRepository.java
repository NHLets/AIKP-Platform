package org.afdb.aikp.modules.person.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.person.infrastructure.persistence.entity.PersonEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for PersonEntity.
 */
@Repository
public interface PersonJpaRepository
        extends JpaRepository<PersonEntity, UUID> {

    List<PersonEntity> findByOrganizationId(
            UUID organizationId);

    List<PersonEntity> findByActiveTrue();
}
