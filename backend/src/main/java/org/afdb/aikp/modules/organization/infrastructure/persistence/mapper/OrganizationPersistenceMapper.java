package org.afdb.aikp.modules.organization.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationType;
import org.afdb.aikp.modules.organization.infrastructure.persistence.entity.OrganizationEntity;

import java.util.UUID;

/**
 * Maps Organization aggregates to and from JPA entities.
 */
public final class OrganizationPersistenceMapper {

    private OrganizationPersistenceMapper() {
    }

    /**
     * Creates a new JPA entity.
     * Used only during CREATE.
     */
    public static OrganizationEntity toEntity(
            Organization organization) {

        if (organization == null) {
            return null;
        }

        return new OrganizationEntity(
                organization.getOrganizationId().getValue(),
                organization.getCode().getValue(),
                organization.getName().getValue(),
                organization.getType(),
                organization.getCountryId().getValue(),
                organization.isActive()
        );
    }

    /**
     * Updates an existing managed JPA entity.
     * Used only during UPDATE.
     */
    public static void updateEntity(
            OrganizationEntity entity,
            Organization organization) {

        if (entity == null || organization == null) {
            return;
        }

        entity.setCode(
                organization.getCode().getValue());

        entity.setName(
                organization.getName().getValue());

        entity.setType(
                organization.getType());

        entity.setCountryId(
                organization.getCountryId().getValue());

        entity.setActive(
                organization.isActive());

        /*
         * Never update:
         *  - id
         *  - version
         *  - createdAt
         *  - createdBy
         *  - updatedAt
         *  - updatedBy
         *
         * Hibernate manages them automatically.
         */
    }

    /**
     * Converts a JPA entity into a domain aggregate.
     */
    public static Organization toDomain(
            OrganizationEntity entity) {

        if (entity == null) {
            return null;
        }

        return Organization.restore(
                OrganizationId.of(entity.getId()),
                OrganizationCode.of(entity.getCode()),
                OrganizationName.of(entity.getName()),
                entity.getType(),
                CountryId.of(entity.getCountryId()),
                entity.isActive()
        );
    }
}
